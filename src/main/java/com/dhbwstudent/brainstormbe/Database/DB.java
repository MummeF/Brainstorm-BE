package com.dhbwstudent.brainstormbe.Database;

import com.dhbwstudent.brainstormbe.model.Comment;
import com.dhbwstudent.brainstormbe.model.Contribution;
import com.dhbwstudent.brainstormbe.model.RoomModel;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DB {

    private static Connection getConnection() {
        // URI enthält alle Informationen zur Datenbank
        try {
            URI dbUri = new URI("postgres://rhbihpjdnxsfdr:1fa5c355a80498fe6d87f7ed43bef896ec8e6dcdece2f22cd74f38483d8f5ae9@ec2-3-223-21-106.compute-1.amazonaws.com:5432/ddku9gq08m6b9o");

            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

            return DriverManager.getConnection(dbUrl, username, password);
        } catch (Exception e) {
            log.error("Error occured while connection to db:", e);
            return null;
        }
    }

    private static Connection conn = getConnection();

    public static void saveRoom(RoomModel room) throws SQLException, URISyntaxException {

        // Verbindung zur DB herstellen
        if (conn == null) {
            conn = getConnection();
        }
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

            String addContributions = "insert into Contribution ( id, roomId, content, subject, reputation) values (" + conId + ", " + roomId + ", '" + content + "', '" + subject + "', '" + reputation + "');";
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

        // Verbindung zur DB herstellen
        if (conn == null) {
            conn = getConnection();
        }
        Statement stmt = conn.createStatement();

        // Raum holen
        String topic = null;
        String description = null;
        String getRoom = "select * from room r where r.id =" + roomId + ";";
        try {
            ResultSet rs = stmt.executeQuery(getRoom);
            if (rs.next()) {
                topic = rs.getString("topic");
                description = rs.getString("description");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        // Beiträge holen
        ArrayList<Contribution> contributions = new ArrayList<>();
        String getContribution = "select * from contribution c where c.roomId =" + roomId + ";";
        try {
            ResultSet rs = stmt.executeQuery(getContribution);
            while (rs.next()) {
                // Einzelnen Beitrag holen
                String conContent = rs.getString("content");
                String conSubject = rs.getString("subject");
                int conReputation = rs.getInt("reputation");
                Long conId = Long.parseLong(rs.getString("id"));

                // Kommentare zu einem Beitrag holen
                Statement comStmt = conn.createStatement();
                String getComment = "select * from comment c where c.contributionId =" + conId + " AND roomId = " + roomId + " order by c.contributionId;";
                ResultSet comRs = comStmt.executeQuery(getComment);
                List<Comment> comments = new ArrayList<>(); //comments muss für jeden Beitrag jeweils neu initialisiert werden
                while (comRs.next()) {
                    // Einzelnen Kommentar holen
                    String comContent = comRs.getString("content");
                    int comReputation = comRs.getInt("reputation");
                    int comId = comRs.getInt("id");
                    if (comContent != null) {
                        comments.add(new Comment(comId, comContent, comReputation));
                    }
                }
                contributions.add(new Contribution(conContent, conSubject, conReputation, comments, conId));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        RoomModel response = RoomModel.builder()
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
        if (conn == null) {
            conn = getConnection();
        }
        Statement stmt = conn.createStatement();

        // Schauen ob Raum vorhanden
        try {
            ResultSet rs = stmt.executeQuery(getRoom);
            if (rs.next()) {
                anzahl = rs.getInt("anzahl");
                return anzahl == 1;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("error occured while closing connection:", e);
            }
        }
    }
}