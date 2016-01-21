package transaction_service;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.io.File;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest
public final class TransactionControllerIntegrationTest {
    private MockMvc mvc;
    private Transaction[] validTransactions;
    private ObjectMapper mapper = new ObjectMapper();

    long transactionCounter = 0;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();

        try
        {
            validTransactions = mapper.readValue(new File("src/test/java/transaction_service/validTransactions.json"), Transaction[].class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createAndGetTransaction() throws Exception {
        // create (type one)
        long transactionID = 0;
        String validContent = mapper.writeValueAsString(validTransactions[0]);

        mvc.perform(put("/transactionservice/transaction/" + transactionID)
                .content(validContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // get
        mvc.perform(get("/transactionservice/transaction/" + transactionID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(validContent));
    }

    @Test
    public void getByTypes() throws Exception {
        // create (type two)
        long firstTransactionID = 1;
        String validContent = mapper.writeValueAsString(validTransactions[1]);

        mvc.perform(put("/transactionservice/transaction/" + firstTransactionID)
                .content(validContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // create (type two)
        long secondTransactionID = 2;
        validContent = mapper.writeValueAsString(validTransactions[2]);

        mvc.perform(put("/transactionservice/transaction/" + secondTransactionID)
                .content(validContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // get by type (type two)
        String expectedResult = String.format("[%d,%d]", firstTransactionID, secondTransactionID);

        mvc.perform(get("/transactionservice/types/" + validTransactions[2].type)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));
    }

    @Test
    public void sumByParent() throws Exception {
        // create (parent 1)
        long firstTransactionID = 3;
        String validContent = mapper.writeValueAsString(validTransactions[3]);

        mvc.perform(put("/transactionservice/transaction/" + firstTransactionID)
                .content(validContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // create (parent 1)
        long secondTransactionID = 4;
        validContent = mapper.writeValueAsString(validTransactions[4]);

        mvc.perform(put("/transactionservice/transaction/" + secondTransactionID)
                .content(validContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // sum by parent (parent 1)
        String expectedResult = String.format("{\"sum\":%1$,.2f}", validTransactions[3].amount + validTransactions[4].amount);

        mvc.perform(get("/transactionservice/sum/" + validTransactions[3].parent_id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));
    }
}

