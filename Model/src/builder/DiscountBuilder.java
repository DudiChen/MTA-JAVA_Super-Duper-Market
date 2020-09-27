package builder;

import entity.Discount;
import jaxb.generated.SDMDiscount;
import jaxb.generated.SDMOffer;

import javax.xml.bind.ValidationException;
import java.util.stream.Collectors;

public class DiscountBuilder implements Builder<SDMDiscount, Discount> {
    @Override
    public Discount build(SDMDiscount source) {
        return new Discount(
                source.getName(),
                source.getIfYouBuy().getItemId(),
                source.getIfYouBuy().getQuantity(),
                Discount.DiscountOperator.getOperatorByString(source.getThenYouGet().getOperator()),
                source.getThenYouGet().getSDMOffer()
                        .stream()
                        .map(sdmOffer -> new Discount.Offer(source.getName(), sdmOffer.getItemId(), sdmOffer.getQuantity(), sdmOffer.getForAdditional()))
                        .collect(Collectors.toList())
        );
    }
}
