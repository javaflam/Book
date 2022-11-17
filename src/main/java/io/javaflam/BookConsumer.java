package io.javaflam;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import io.smallrye.reactive.messaging.kafka.Record;

@ApplicationScoped
public class BookConsumer {

    private static final Logger LOGGER = Logger.getLogger("BookConsumer");

    @Channel("published-books-out")
    Emitter<Record<String, Book>> emitter;

    @Incoming("books-in")
    public void receive(Record<String, Book> record) {
        LOGGER.infof("Received book: %s - %s (%s)",
                record.value().getIsbn(), record.value().getTitle(), record.value().getAuthor());
        // Process the book
        emitter.send(record);
    }
}
