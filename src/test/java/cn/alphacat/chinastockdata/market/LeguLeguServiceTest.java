package cn.alphacat.chinastockdata.market;

import cn.alphacat.chinastockdata.enums.LuguLuguIndexPEEnums;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ResourceLoader;

class LeguLeguServiceTest {
  @InjectMocks private LeguLeguService leguLeguService;
  @Mock private ResourceLoader resourceLoader;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getStockIndexPE() {
    try {
      leguLeguService.getStockIndexPE(LuguLuguIndexPEEnums.SCI300);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
