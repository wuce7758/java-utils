package com.github.bingoohuang.utils.xml;

import org.junit.Test;

import static com.github.bingoohuang.utils.xml.Xmls.MarshalOption.PrettyFormat;
import static com.github.bingoohuang.utils.xml.Xmls.MarshalOption.WithXmlDeclaration;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class XmlsTest {
    @Test
    public void getCarAsXml() {
        String registration = "abc123";
        String brand = "沃尔沃";
        String description = "Sedan<xx.yy@gmail.com>";

        Car car = new Car(registration, brand, description);
        String xml = Xmls.marshal(car);
        assertThat(xml, is(equalTo("<car registration=\"abc123\"><brand>沃尔沃</brand><description><![CDATA[Sedan<xx.yy@gmail.com>]]></description></car>")));
        System.out.println(xml);
        xml = Xmls.marshal(car, PrettyFormat);
        System.out.println(xml);
        xml = Xmls.marshal(car, WithXmlDeclaration);
        System.out.println(xml);
        xml = Xmls.marshal(car, PrettyFormat, WithXmlDeclaration);
        System.out.println(xml);
    }
}
