package net.breezeware.dataStore;

import net.breezeware.entity.Delivery;
import net.breezeware.exception.CustomException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class DeliveryDataStore {
    private static final String DB_NAME = "cos";
    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/" + DB_NAME;
    private static final String DB_USER = "cos_usr";
    private static final String DB_PASSWORD = "P@ssw0rd";

    private static final String TABLE_DELIVERY = "delivery";
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_ORDER_DELIVERY_LOCATION = "delivery_location";
    private static final String COLUMN_ORDER_DELIVERY_DATE_TIME = "delivery_date_time";

    private static final String INSERT_INTO_DELIVERY = "INSERT INTO " + TABLE_DELIVERY + "(" + COLUMN_ORDER_ID + "," +
            COLUMN_ORDER_DELIVERY_LOCATION + "," + COLUMN_ORDER_DELIVERY_DATE_TIME + ") VALUES(?,?,?)";

    private static final String QUERY_DELIVERY_RECORD = "SELECT * FROM " + TABLE_DELIVERY + " WHERE "
            + COLUMN_ORDER_ID + " = ?";

    private Connection connection;
    public void openConnection() throws CustomException {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new CustomException("Couldn't Connect to Database: " + e.getMessage());
        }
    }

    public void closeConnection() throws CustomException {
        try {
            if (!Objects.isNull(connection)){
                connection.close();
            }
        } catch (SQLException e) {
            throw new CustomException("Couldn't Close Connection: " + e.getMessage());
        }
    }

    public Delivery insertIntoDelivery(int orderId, String deliveryLocation, LocalDateTime deliveryDateTime) throws CustomException {
        try(PreparedStatement insertIntoDelivery = connection.prepareStatement(INSERT_INTO_DELIVERY)){
            insertIntoDelivery.setInt(1,orderId);
            insertIntoDelivery.setString(2,deliveryLocation);
            insertIntoDelivery.setTimestamp(3, Timestamp.valueOf(deliveryDateTime));
            insertIntoDelivery.executeUpdate();
            return queryDeliveryRecord(orderId);
        } catch (SQLException e){
            throw new CustomException("Couldn't Insert into Delivery. " + e.getMessage());
        }
    }

    public Delivery queryDeliveryRecord(int orderId) throws CustomException {
        try(PreparedStatement queryDeliveryRecord = connection.prepareStatement(QUERY_DELIVERY_RECORD)){
            queryDeliveryRecord.setInt(1, orderId);
            ResultSet resultSet = queryDeliveryRecord.executeQuery();
            if(resultSet.next()){
                Delivery delivery = new Delivery();
                delivery.setOrder_id(resultSet.getInt(COLUMN_ORDER_ID));
                delivery.setOrderLocation(resultSet.getString(COLUMN_ORDER_DELIVERY_LOCATION));
                delivery.setOrderDateTime(resultSet.getTimestamp(COLUMN_ORDER_DELIVERY_DATE_TIME).toInstant());
                return delivery;
            }else {
                resultSet.close();
                throw new CustomException("Delivery Record not found for Order Id: " + orderId);
            }
        }catch (SQLException e){
            throw new CustomException("Couldn't Query for Delivery Record. " + e.getMessage());
        }
    }
}
