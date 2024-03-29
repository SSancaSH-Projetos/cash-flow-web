package io.github.mds.cashflowweb.travel;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mds.cashflowweb.NoSecurityConfiguration;
import io.github.mds.cashflowweb.employee.Employee;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TravelController.class)
@Import(NoSecurityConfiguration.class)
public class TravelControllerTests {

    @Autowired
    private MockMvc client;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TravelService travelService;

    @Nested
    class CreateTravelTests {

        @Test
        void doNotCreateTravelWithInvalidFields() throws Exception {
            // given
            var travel = TravelFactory.createInvalidTravelRequest();
            // when
            var result = client.perform(post("/api/travels")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(travel))
            );
            // then
            result.andExpect(status().isBadRequest());
        }

    }

    @Nested
    class FindTravelTests {

        // TODO: implement test case when travel doesn't exists

    }

    @Nested
    class UpdateTravelTests {

        // TODO: fix this test, probably by configuring security
        @Test
        void doNotUpdateNonexistentTravel() throws Exception {
            // given
            var travel = TravelFactory.createRandomTravelRequest();
            doThrow(TravelNotFoundException.class).when(travelService).updateTravel(anyLong(), any(Travel.class), any(Employee.class));
            // when
            var result = client.perform(put("/api/travels/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(travel))
            );
            // then
            result.andExpect(status().isNotFound());
        }

    }

    @Nested
    class DeleteTravelTests {

        // TODO: fix this test, probably by configuring security
        @Test
        void doNotDeleteNonexistentTravel() throws Exception {
            // given
            doThrow(TravelNotFoundException.class).when(travelService).deleteTravel(anyLong(), any(Employee.class));
            // when
            var result = client.perform(delete("/api/travels/{id}", 1));
            // then
            result.andExpect(status().isNotFound());
        }

    }

}
