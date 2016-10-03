package com.gmal.sobol.i.stanislav.news4pda;

/**
 * Created by VZ on 02.10.2016.
 */


import com.gmal.sobol.i.stanislav.news4pda.data.parser.BaseParser;
import com.gmal.sobol.i.stanislav.news4pda.dto.DetailsMainDTO;
import com.gmal.sobol.i.stanislav.news4pda.dto.ItemDTO;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void test_all() {
        final BaseParser parser = new BaseParser();

        for (int i = 0; i < 5; i++) {
            final List<ItemDTO> items = parser.getPageData(i, null);
            assertTrue(items != null);
            assertTrue(items.size() > 0);
            for (int j = 0; j < items.size() / 2; j++) {
                final ItemDTO itemDTO = items.get(j);
                assertTrue(itemDTO.isValid());

                DetailsMainDTO detailsMainDTO = null;
                try {
                    detailsMainDTO = parser.getDetailedData(itemDTO.getDetailURL(), null);
                } catch (java.lang.IndexOutOfBoundsException e) {
                    assertTrue(true);
                }

                assertTrue(detailsMainDTO != null);
                assertTrue(detailsMainDTO.isValid());
            }
        }
    }
}
