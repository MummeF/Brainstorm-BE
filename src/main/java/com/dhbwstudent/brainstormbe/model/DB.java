package com.dhbwstudent.brainstormbe.model;

import com.dhbwstudent.brainstormbe.api.main.MainService;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class DB {

    static public int roomID = 4711;

    public static void main(String[] args) throws SQLException, URISyntaxException {

        //connectToAndQueryDatabase("User", "123");
        /*
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from Room;");
        while (rs.next()) {
            int x = rs.getInt("id");
            String s = rs.getString("topic");
            float f = rs.getFloat("state");
            System.out.println("Hallo Welt" + x + s + f);
        }
        */

        //RoomModel room = new RoomModel();
        //saveRoom(room);
        getRoom(roomID);

    }
    /*
    private static Connection getConnection() throws URISyntaxException, SQLException {
        String dbUrl = "postgres://rhbihpjdnxsfdr:1fa5c355a80498fe6d87f7ed43bef896ec8e6dcdece2f22cd74f38483d8f5ae9@ec2-3-223-21-106.compute-1.amazonaws.com:5432/ddku9gq08m6b9o";
        return DriverManager.getConnection(dbUrl);
    }
    */

    private static Connection getConnection() throws URISyntaxException, SQLException {
        //URI dbUri = new URI(System.getenv("DATABASE_URL"));
        URI dbUri = new URI("postgres://rhbihpjdnxsfdr:1fa5c355a80498fe6d87f7ed43bef896ec8e6dcdece2f22cd74f38483d8f5ae9@ec2-3-223-21-106.compute-1.amazonaws.com:5432/ddku9gq08m6b9o");

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }

    public static void saveRoom(RoomModel room) throws SQLException, URISyntaxException {



        Connection conn = getConnection();
        Statement stmt = null;

        String roomStr = room.toString();
        System.out.println("Room: " + roomStr);

        //Irgendwie auf Attribute von Room zugreifen

        String addRoom =  "insert into Room (id, topic, state, ispublic, password)" +
                "values (" + roomID + ", 'Testtopic', 'status', '1', 'geheim');";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(addRoom);
        } catch (SQLException e ) {
            System.out.println(e);
        }

        //while("Anzahl an Contributions > 0") {
        String addContributions =  "insert into Contribution (id, roomid, contributionidnr, content) values (1, '" + roomID +"', '1', 'Beitrag1');" +
                "insert into Contribution (id, roomid, contributionidnr, content) values (2, '" + roomID +"', '2', 'Beitrag2');";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(addContributions);
        } catch (SQLException e ) {
            System.out.println(e);
        } finally {
            if (stmt != null) { stmt.close(); }
        }

    }

    public static void getRoom(long id) throws SQLException, URISyntaxException {

        Connection conn = getConnection();
        Statement stmt = null;

        String getRoom =  "select * from Contribution c inner join room r on (r.id = c.roomid) where r.id =" + id +";";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getRoom);
            while (rs.next()) {
                String topic = rs.getString("topic");
                String state = rs.getString("state");
                boolean ispublic = rs.getBoolean("ispublic");
                String password = rs.getString("password");
                System.out.println(topic + "\t" + state +
                        "\t" + ispublic + "\t" + password);
            }
        } catch (SQLException e ) {
            System.out.println(e);
        }

    }
}
