package elements;

import managers.DatabaseManager;
import utils.Ansi;
import utils.StringPrintWriter;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Group extends AbstractGroup {

    public Group(int owner, String groupname, String name) {
        this.groupname = groupname;
        this.name = name;
        this.id = groupname.hashCode();
        this.owner = owner;
        this.admins.add(this.owner);
        this.members.add(this.owner);
    }

    @Override
    public void includeUser(int id) {

    }

    @Override
    public void excludeUser(int id) {

    }

    public static boolean groupExists(String groupname) {
        String sql = "SELECT EXISTS(SELECT 1 FROM groups WHERE groupname = ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, groupname);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }

    public static void addGroup(User owner, Group group) {
        String sql = "INSERT INTO groups (groupname, name, type, members, admins, owner_id)\n"
                + "VALUES (?, ?, ?, ?, ?, ?);";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, group.groupname);
            stmt.setString(2, group.groupname);
            stmt.setInt(3, 0);

            Integer[] membersArray = {owner.id};
            Array members = conn.createArrayOf("INTEGER", membersArray);

            stmt.setArray(4, members);
            stmt.setArray(5, members);
            stmt.setInt(6, owner.id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error adding Group: " + e.getMessage());
        }
    }

    public static Group register(
        StringPrintWriter out, User owner, String groupname, String name
    ) {
        if (groupname.length() > ServerData.MAX_USERNAME_LENGTH) {
            out.println(Ansi.Colors.RED.apply(
                "Groupname is too long. Max: " + ServerData.MAX_USERNAME_LENGTH
            ));
            return null;
        }

        if (groupExists(groupname)) {
            out.println(Ansi.Colors.RED.apply("Groupname is already in use."));
            return null;
        }
        Group group = new Group(owner.getUserId(), groupname, name);
        out.println("Registered successfully.");
        addGroup(owner, group);
        ServerData.addGroup(group);

        return group;
    }

    public static ArrayList<Integer> getArrayListFromPgArray(Array sqlArray) throws SQLException {
        if (sqlArray == null) return new ArrayList<>();
        Integer[] array = (Integer[]) sqlArray.getArray();
        return new ArrayList<>(Arrays.asList(array));
    }

    public static Group getGroupByName(String groupname) {
        String sql = "SELECT * FROM groups WHERE groupname = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, groupname);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int owner = rs.getInt(7);
                String name = rs.getString(3);
                Group group = new Group(owner, groupname, name);

                group.id = rs.getInt(1);
                group.members = getArrayListFromPgArray(rs.getArray(5));
                group.admins = getArrayListFromPgArray(rs.getArray(6));

                return group;
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }

        return null;
    }

    public void addMember(String username) {
        String sql = "UPDATE groups SET members = array_append(members, ?) WHERE id = ?";

        int id = User.getUserIdByUsername(username);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setInt(2, this.id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error adding member: " + e.getMessage());
        }
    }

    public void invite(StringPrintWriter out, String username) {
        if (!User.userExists(username)) {
            out.println(Ansi.Colors.RED.apply("User not found."));
            return;
        }
        addMember(username);
        out.println(username + " invited to " + name);
    }
}
