package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.samples.petclinic.web.OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author André Félix
 * @date 08/03/2021 19:38
 */
@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
class OwnerControllerTest {

    @Autowired
    OwnerController ownerController;

    @Autowired
    ClinicService clinicService;

    MockMvc mockMvc;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    /**
     * to reset clinicService
     */
    @AfterEach
    void tearDown() {
        reset(clinicService);
    }

    @Test
    void initCreationFormTest() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name(VIEWS_OWNER_CREATE_OR_UPDATE_FORM));
    }

    @Test
    void findByNameNotFoundTest() throws Exception {
        // param method is really helpful and powerful. it binds lastName instance from
        // Person Class
        mockMvc.perform(get("/owners")
                .param("lastName", "Don't find ME!"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));

    }

    @Test
    void testReturnListOfOwners() throws Exception {
        // GIVEN

        given(clinicService.findOwnerByLastName("")).
                willReturn(Lists.newArrayList(new Owner(), new Owner()));

        // WHEN
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"));

        // THEN
        then(clinicService).should().findOwnerByLastName(stringArgumentCaptor.capture());

        assertThat(stringArgumentCaptor.getValue()).isEqualToIgnoringCase("");

    }

    @Test
    void findByNameOneFoundTest() throws Exception {
        Owner justOne = new Owner();
        justOne.setId(1);
        final String findJustOne = "FindJustOne";
        justOne.setLastName(findJustOne);

        given(clinicService.findOwnerByLastName(findJustOne)).
                willReturn(Lists.newArrayList(justOne));

        mockMvc.perform(get("/owners")
                .param("lastName", findJustOne))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));

        then(clinicService).should().findOwnerByLastName(anyString());

    }
}