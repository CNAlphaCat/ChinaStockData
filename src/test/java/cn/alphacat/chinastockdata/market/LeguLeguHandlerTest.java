package cn.alphacat.chinastockdata.market;

import cn.alphacat.chinastockdata.enums.LuguLuguIndexPEEnums;
import cn.alphacat.chinastockdata.market.handler.LeguLeguHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ResourceLoader;

class LeguLeguHandlerTest {
  @InjectMocks private LeguLeguHandler leguLeguHandler;
  @Mock private ResourceLoader resourceLoader;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getStockIndexPE() {
    try {
      leguLeguHandler.getStockIndexPE(LuguLuguIndexPEEnums.SCI300);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
