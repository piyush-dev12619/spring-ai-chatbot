package com.piyush.firstproject.springai.ChatController;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vector")
public class VectorController {

    private final VectorStore vectorStore;

    public VectorController(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostMapping("/add")
    public String addDocument(@RequestBody String text) {

        Document document = new Document(text);

        vectorStore.add(List.of(document));

        return "Document stored successfully";
    }

}