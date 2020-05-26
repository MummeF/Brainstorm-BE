package com.dhbwstudent.brainstormbe.model;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

        // Verbindung zur DB herstellen
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();

        // Raum speichern
        long roomId = room.getId();
        String topic = room.getTopic() != null ? room.getTopic() : "";
        String description = room.getDescription() != null ? room.getDescription() : "";

        String addRoom = "insert into Room (id, topic, description) values (" + roomId + ", '" + topic + "', '" + description + "');";
        try {
            stmt.executeQuery(addRoom);
        } catch (SQLException e) {
            System.out.println(e);
        }

        // Beiträge speichern
        ArrayList<Contribution> contributions = room.getContributions();
        for (Contribution contribution : contributions) {
            long conId = contribution.getId();
            String content = contribution.getContent() != null ? contribution.getContent() : "";
            String subject = contribution.getSubject() != null ? contribution.getSubject() : "";
            int reputation = contribution.getReputation();

            String addContributions = "insert into Contribution ( id, roomId, content, subject, reputation) values (" + conId +", " +roomId + ", '" + content + "', '" + subject + "', '" + reputation + "');";
            try {
                stmt.executeQuery(addContributions);
            } catch (SQLException e) {
                System.out.println(e);
            }

            // Kommentare speichern
            List<Comment> comments = contribution.getComments();
            for (Comment comment : comments) {
                int comId = comment.getId();
                String comContent = comment.getContent() != null ? comment.getContent() : "";
                int comReputation = comment.getReputation();

                String addComments = "insert into Comment ( id, roomId, contributionId, content, reputation) values (" + comId + ", " + roomId + "," + conId + ", '" + comContent + "', '" + comReputation + "');";
                try {
                    stmt.executeQuery(addComments);
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
    }

    public static RoomModel getRoom(long roomId) throws SQLException, URISyntaxException {

        // Room
        RoomModel response;
        String topic = null;
        String description = null;

        // Contribution
        ArrayList<Contribution> contributions = new ArrayList<>();
        String conContent;
        String conSubject;
        int conReputation;
        Long conId = null;

        // Comment
        List<Comment> comments;
        String comContent;
        int comReputation;
        int comId;

        // Verbindung zur DB herstellen
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();

        // SQL Abfragen
        String getRoom = "select * from room r where r.id =" + roomId + ";";
        String getContribution = "select * from contribution c where c.roomId =" + roomId + ";";
        String getComment = "select * from comment c where c.contributionId =" + conId + " order by c.contributionId;";

        // Raum holen
        try {
            ResultSet rs = stmt.executeQuery(getRoom);
            if(rs.next()) {
                topic = rs.getString("topic");
                description = rs.getString("description");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        // Beiträge holen
        try {
            ResultSet rs = stmt.executeQuery(getContribution);
            while (rs.next()) {
                // Einzelnen Beitrag holen
                conContent = rs.getString("content");
                conSubject = rs.getString("subject");
                conReputation = Integer.parseInt(rs.getString("reputation"));
                conId = Long.parseLong(rs.getString("id"));

                // Kommentare zu einem Beitrag holen
                Statement comStmt = conn.createStatement();
                ResultSet comRs = comStmt.executeQuery(getComment);
                comments = new ArrayList<>(); //comments muss für jeden Beitrag jeweils neu initialisiert werden
                while (comRs.next()) {
                    // Einzelnen Kommentar holen
                    comContent = comRs.getString("content");
                    comReputation = Integer.parseInt(comRs.getString("reputation"));
                    comId = Integer.parseInt(comRs.getString("id"));
                    if(comContent!=null) {
                        comments.add(new Comment(comId, comContent, comReputation));
                    }
                }
                contributions.add(new Contribution(conContent, conSubject, conReputation, comments, conId));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        response = RoomModel.builder()
            .id(roomId)
            .topic(topic != null ? topic : "")
            .contributions(contributions)
            .description(description != null ? description : "")
            .build();

        return response;
    }

    public static boolean roomExists(long roomId) throws SQLException, URISyntaxException {
        String getRoom = "select COUNT(*) AS anzahl FROM room r WHERE r.id =" + roomId + ";";
        int anzahl;

        // Verbindung zur DB herstellen
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();

        // Schauen ob Raum vorhanden
        try {
            ResultSet rs = stmt.executeQuery(getRoom);
            if(rs.next()) {
                anzahl = rs.getInt("anzahl");
                return anzahl == 1;
            }else{
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
}