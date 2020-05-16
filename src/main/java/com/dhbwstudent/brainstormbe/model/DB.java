package com.dhbwstudent.brainstormbe.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;

public class DB {

    //Für Testzwecke, da nicht auf die echte RoomID zugegriffen werden kann
    static public int roomID = 4711;

    public static void main(String[] args) throws SQLException, URISyntaxException {

        /*
        Contribution con1 = new Contribution();
        con1.setContent("Beitrag1");
        con1.setId(1);

        Contribution con2 = new Contribution();
        con2.setContent("Beitrag2");
        con2.setId(2);

        RoomModel room = new RoomModel();
        room.setPublic(true);
        room.setTopic("Thema");
        room.addContribution(con1);
        room.addContribution(con2);
        saveRoom(room);
         */

        //getRoom(roomID);

    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        //Wenn Java auf Heroku läuft ist URI in Systemvariable gesetzt
        //URI dbUri = new URI(System.getenv("DATABASE_URL"));

        //URI kann sich ändern
        URI dbUri = new URI("postgres://rhbihpjdnxsfdr:1fa5c355a80498fe6d87f7ed43bef896ec8e6dcdece2f22cd74f38483d8f5ae9@ec2-3-223-21-106.compute-1.amazonaws.com:5432/ddku9gq08m6b9o");

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }

    public static void saveRoom(RoomModel room) throws SQLException, URISyntaxException {

        Connection conn = getConnection();
        Statement stmt = null;

        long id = room.getId();
        String topic = room.getTopic();
        State state = room.getState();
        boolean isPublic = room.isPublic();
        ArrayList<Contribution> contributions = room.getContributions();

        //System.out.println(id);
        //System.out.println(topic);
        //System.out.println(state);
        //System.out.println(isPublic);
        //System.out.println(contributions);

        String addRoom =  "insert into Room (id, topic, state, ispublic)" +
                "values (" + roomID + ", '"+ topic +"', '"+ state +"', '"+ isPublic +"');";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(addRoom);
        } catch (SQLException e ) {
            System.out.println(e);
        }

        while(!contributions.isEmpty()) {
            Contribution con = contributions.get(0);
            long conId = con.getId();
            String content = con.getContent();
            contributions.remove(0);

            String addContributions = "insert into Contribution (id, roomid, contributionidnr, content) values (+"+ conId +", '" + roomID + "', '"+ conId +"', '"+ content +"');";

            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(addContributions);
            } catch (SQLException e) {
                System.out.println(e);
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
    }

    public static RoomModel getRoom(long id) throws SQLException, URISyntaxException {

        String topic = null;
        String state = null;
        boolean ispublic = false;
        String password = null;
        int i = 0;

        RoomModel room = new RoomModel();

        Connection conn = getConnection();
        Statement stmt = null;

        String getRoom =  "select * from Contribution c inner join room r on (r.id = c.roomid) where r.id =" + id +";";
        //System.out.println(getRoom);

        ArrayList<Contribution> contributions = null;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getRoom);
            while (rs.next()) {
                //Room
                topic = rs.getString("topic");
                state = rs.getString("state");
                ispublic = rs.getBoolean("ispublic");
                password = rs.getString("password");
                //System.out.println(topic + "\t" + state +
                //        "\t" + ispublic + "\t" + password);

                //Contributions
                String content = rs.getString("content");
                Long contributionIdNr = Long.parseLong(rs.getString("contributionIdNr"));
                Long conId = Long.parseLong(rs.getString("id"));

                Contribution con = new Contribution();
                con.setContent(content);
                con.setId(conId);

                room.addContribution(con);
            }
        } catch (SQLException e ) {
            System.out.println(e);
        }

        room.setPublic(ispublic);
        room.setTopic(topic);
        //room.addContribution(contributions);

        //System.out.println(room.toString());
        return room;
    }
}
