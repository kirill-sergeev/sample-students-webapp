package com.sergeev.studapp.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.*;
import com.sergeev.studapp.model.Constants;

import javax.naming.InitialContext;
import javax.naming.NamingException;


public class MongoDaoFactory extends DaoFactory {

    private static final MongoDaoFactory MONGO_DAO_FACTORY = new MongoDaoFactory();
    private static MongoClient mongoClient;

    static{
        try {
            mongoClient = (MongoClient) new InitialContext().lookup("java:comp/env/mongodb");
        } catch (NamingException e) {
            //Off while dev.
            //throw new IllegalStateException("Missing JNDI!", e);
        }
    }

    private MongoDaoFactory(){}

    public static MongoDatabase getConnection() {
        /////Only for development!///////
        if (mongoClient == null){
            ServerAddress address = new ServerAddress("localhost", 27017);
            MongoClientOptions options = MongoClientOptions.builder()
                    .writeConcern(WriteConcern.ACKNOWLEDGED).build();
            mongoClient = new MongoClient(address, options);
            return mongoClient.getDatabase(Constants.DATABASE);
        }
        ////////////////////////////////
        return mongoClient.getDatabase(Constants.DATABASE);
    }

    public static MongoDaoFactory getInstance(){
        return MONGO_DAO_FACTORY;
    }

    @Override
    public AccountDao getAccountDao() {
        return new MongoAccountDao();
    }
    @Override
    public CourseDao getCourseDao() {
        return new MongoCourseDao();
    }
    @Override
    public DisciplineDao getDisciplineDao() {
        return new MongoDisciplineDao();
    }
    @Override
    public GroupDao getGroupDao() {
        return new MongoGroupDao();
    }
    @Override
    public LessonDao getLessonDao() {
        return new MongoLessonDao();
    }
    @Override
    public MarkDao getMarkDao() {
        return new MongoMarkDao();
    }
    @Override
    public UserDao getUserDao() {
        return new MongoUserDao();
    }

}
