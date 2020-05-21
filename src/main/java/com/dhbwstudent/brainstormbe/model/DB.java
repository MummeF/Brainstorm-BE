package com.dhbwstudent.brainstormbe.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;


public class DB {

    private static Connection getConnection() throws URISyntaxException, SQLException {
        //Wenn Backend auf Heroku läuft ist URI in Systemvariable gesetzt
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

        long roomId = room.getId();
        String topic = room.getTopic();
        String description = room.getDescription();
        ArrayList<Contribution> contributions = room.getContributions();

        String addRoom = "insert into Room (id, topic, description) values (" + roomId + ", '" + topic + "', '" + description + "');";
        try {
            stmt = conn.createStatement();
            stmt.executeQuery(addRoom);
        } catch (SQLException e) {
            System.out.println(e);
        }

        for (Contribution contribution : contributions) {
            long conId = contribution.getId();
            String content = contribution.getContent();
            String subject = contribution.getSubject();

            String addContributions = "insert into Contribution ( id, roomId, content, subject) values (" + conId +", " +roomId + ", '" + content + "', '" + subject + "');";

            try {
                stmt = conn.createStatement();
                stmt.executeQuery(addContributions);
            } catch (SQLException e) {
                System.out.println(e);
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
    }

    public static RoomModel getRoom(long roomId) throws SQLException, URISyntaxException {

        Connection conn = getConnection();
        Statement stmt = null;

        String getRoom = "select * from Contribution c inner join room r on (r.id = c.roomid) where r.id =" + roomId + ";";

        ArrayList<Contribution> contributions = new ArrayList<>();

        RoomModel response = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getRoom);
            String topic = null;
            String description = null;
            while (rs.next()) {
                //Room
                topic = rs.getString("topic");
                description = rs.getString("description");
                //Contributions
                String content = rs.getString("content");
                String subject = rs.getString("subject");
                Long conId = Long.parseLong(rs.getString("id"));

                contributions.add(new Contribution(content, subject, conId));
            }
            response = RoomModel.builder()
                    .id(roomId)
                    .topic(topic != null ? topic : "")
                    .contributions(new ArrayList<>())
                    .description(description != null ? description : "")
                    .build();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return response;
    }
}