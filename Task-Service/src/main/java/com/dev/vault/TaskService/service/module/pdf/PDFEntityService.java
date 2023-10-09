package com.dev.vault.TaskService.service.module.pdf;

import com.dev.vault.TaskService.model.entity.Task;
import com.dev.vault.TaskService.repository.TaskRepository;
import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PDFEntityService {

    private final TaskRepository taskRepository;

    public ByteArrayInputStream createTaskPDF() {
        List<Task> taskList = taskRepository.findAll();

        Document document = new Document(PageSize.A5, 36, 36, 65, 36);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
        pdfWriter.setPageEvent(new EntityHeaderAndFooterEventHelper());
        document.open();

        Paragraph paragraph1 = new Paragraph("Tasks");
        document.add(paragraph1);

        taskList.forEach(task -> {
            int counter = 0;
            Paragraph paragraph2 = new Paragraph("", FontFactory.getFont(FontFactory.COURIER, 8));
            paragraph2.add("Task Name: ");
            paragraph2.add(String.valueOf(counter));
            paragraph2.add(task.getTaskName());

            document.add(paragraph2);
            ++counter;
        });

        document.close();
        pdfWriter.close();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

}
