package com.dhbwstudent.brainstormbe.model;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DB {


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

        String addRoom =  "insert into Room (id, topic, state, ispublic)" +
                "values (" + id + ", '"+ topic +"', '"+ state +"', '"+ isPublic +"');";
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

            String addContributions = "insert into Contribution (id, roomid, contributionidnr, content) values (+"+ conId +", '" + id + "', '"+ conId +"', '"+ content +"');";

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


        Connection conn = getConnection();
        Statement stmt = null;

        String getRoom =  "select * from Contribution c inner join room r on (r.id = c.roomid) where r.id =" + id +";";

        ArrayList<Contribution> contributions = new ArrayList<>();


        RoomModel response = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getRoom);
            String topic = null;
            while (rs.next()) {
                //Room
                topic = rs.getString("topic");
                //Contributions
                String content = rs.getString("content");
                Long conId = Long.parseLong(rs.getString("id"));

                contributions.add(new Contribution(content,"",  conId));
            }
            response = RoomModel.builder()
                    .id(id)
                    .topic(topic != null ? topic : "")
                    .contributions(new ArrayList<>())
                    .build();
        } catch (SQLException e ) {
            System.out.println(e);
        }
        return response;
    }
}
