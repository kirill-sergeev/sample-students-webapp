package com.sergeev.studapp.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MongoDaoFactory extends DaoFactory {

    private static final Logger LOG = LoggerFactory.getLogger(MongoDaoFactory.class);
    private static final String DATABASE;
    private static final String ADDRESS;
    private static final int PORT;
    private static final String USER;
    private static final String PASSWORD;
    private static final MongoDaoFactory DAO_FACTORY = new MongoDaoFactory();
    private static final String PROPERTIES_FILE = "db_mongo.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (InputStream resourceStream = classLoader.getResourceAsStream(PROPERTIES_FILE)) {
            PROPERTIES.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DATABASE = PROPERTIES.getProperty("database");
        ADDRESS = PROPERTIES.getProperty("address");
        PORT = Integer.parseInt(PROPERTIES.getProperty("port"));
        USER = PROPERTIES.getProperty("username");
        PASSWORD = PROPERTIES.getProperty("password");
    }

    static MongoDatabase getConnection() {
        ServerAddress address = new ServerAddress(ADDRESS, PORT);
        MongoClientOptions options = MongoClientOptions.builder()
                .writeConcern(WriteConcern.ACKNOWLEDGED).build();
        MongoClient mongoClient = new MongoClient(address, options);
        return mongoClient.getDatabase(DATABASE);
    }

    private MongoDaoFactory(){}

    public static MongoDaoFactory getInstance(){
        return DAO_FACTORY;
    }

    @Override
    public AccountDao getAccountDao() {
        return new MongoAccountDao();
    }
    @Override
    public CourseDao getCourseDao() {
        return null;
    }
    @Override
    public DisciplineDao getDisciplineDao() {
        return null;
    }
    @Override
    public GroupDao getGroupDao() {
        return new MongoGroupDao();
    }
    @Override
    public LessonDao getLessonDao() {
        return null;
    }
    @Override
    public MarkDao getMarkDao() {
        return null;
    }
    @Override
    public UserDao getUserDao() {
        return new MongoUserDao();
    }
}
