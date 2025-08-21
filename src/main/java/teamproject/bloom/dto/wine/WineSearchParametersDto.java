package teamproject.bloom.dto.wine;

import java.math.BigDecimal;
import teamproject.bloom.model.Wine;

public record WineSearchParametersDto(
        String[] title,
        BigDecimal priceFrom,
        BigDecimal priceTo,
        Float[] alcohol,
        Wine.Variety[] variety,
        Wine.Value[] value,
        Integer vintageFrom,
        Integer vintageTo,
        String[] grape,
        String[] region
) {
    public static class Builder {
        private String[] title;
        private BigDecimal priceFrom;
        private BigDecimal priceTo;
        private Float[] alcohol;
        private Wine.Variety[] variety;
        private Wine.Value[] value;
        private Integer vintageFrom;
        private Integer vintageTo;
        private String[] grape;
        private String[] region;

        public Builder title(String... title) {
            this.title = title;
            return this;
        }

        public Builder priceFrom(BigDecimal priceFrom) {
            this.priceFrom = priceFrom;
            return this;
        }

        public Builder priceTo(BigDecimal priceTo) {
            this.priceTo = priceTo;
            return this;
        }

        public Builder alcohol(Float... alcohol) {
            this.alcohol = alcohol;
            return this;
        }

        public Builder variety(Wine.Variety... variety) {
            this.variety = variety;
            return this;
        }

        public Builder value(Wine.Value... value) {
            this.value = value;
            return this;
        }

        public Builder vintageFrom(Integer vintageFrom) {
            this.vintageFrom = vintageFrom;
            return this;
        }

        public Builder vintageTo(Integer vintageTo) {
            this.vintageTo = vintageTo;
            return this;
        }

        public Builder grape(String... grape) {
            this.grape = grape;
            return this;
        }

        public Builder region(String... region) {
            this.region = region;
            return this;
        }

        public WineSearchParametersDto build() {
            return new WineSearchParametersDto(
                    title, priceFrom, priceTo, alcohol, variety, value,
                    vintageFrom, vintageTo, grape, region
            );
        }
    }
}
