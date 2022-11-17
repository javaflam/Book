package io.javaflam;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import io.smallrye.reactive.messaging.kafka.Record;

@Path("/books")
public class BookResource {
    
    private static final Logger LOGGER = Logger.getLogger(BookResource.class);
    
    @Channel("books-out")
    Emitter<Record<String, Book>> emitter;

    @POST
    public Response send(Book book) {
        LOGGER.infof("Sending book %s to Kafka", book.getTitle());
        emitter.send(Record.of(book.getIsbn(), book));
        return Response.accepted().build();
    }
}
