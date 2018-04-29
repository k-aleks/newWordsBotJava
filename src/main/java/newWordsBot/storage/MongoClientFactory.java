package newWordsBot.storage;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class MongoClientFactory {

    public static MongoClient create(String connectionString) {

        CodecRegistry registry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(), registry);

        MongoClientOptions.Builder builder = MongoClientOptions.builder()
                .codecRegistry(codecRegistry);

        MongoClientURI uri = new MongoClientURI(connectionString, builder);

        MongoClient mongoClient = new MongoClient(uri);

        return mongoClient;
    }
}

