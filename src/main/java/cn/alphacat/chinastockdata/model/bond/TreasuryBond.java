package cn.alphacat.chinastockdata.model.bond;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TreasuryBond {
  private LocalDate solarDate;
  private BigDecimal fiveYearTreasuryBondYield;
  private BigDecimal tenYearTreasuryBondYield;
  private BigDecimal thirtyYearTreasuryBondYield;
  private BigDecimal twoYearTreasuryBondYield;

  private BigDecimal twoYearMinusTenYearTreasuryBondYield;

  private BigDecimal twoYearUSTreasuryBondYield;
  private BigDecimal fiveYearUSTreasuryBondYield;
  private BigDecimal tenYearUSTreasuryBondYield;
  private BigDecimal thirtyYearUSTreasuryBondYield;

  private BigDecimal twoYearMinusTenYearUSTreasuryBondYield;

  private BigDecimal chinaGDPGrowthRate;
  private BigDecimal usGDPGrowthRate;
}
