package com.evam.marketing.offer.template.utils;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalTime;
import java.time.ZonedDateTime;

/**
 * The {@code VasOfferUtil} class
 *
 * @author Abdul Wadood
 * @since 8.0.2
 */
public class VasOfferUtil {
    public static boolean isTimeWindowOk(LocalTime start, LocalTime end) {
        LocalTime localTime = ZonedDateTime.now().toLocalTime();
        boolean before = start.isAfter(localTime);
        boolean after = end.isBefore(localTime);

        return before && after;
    }

    public static String hit(String endpoint, String payload) throws IOException {
        HttpPost httpPost = new HttpPost(endpoint);
        httpPost.setEntity(new StringEntity(payload));
        HttpClient client = HttpClients.createDefault();
        HttpResponse httpResponse = client.execute(httpPost);

        return EntityUtils.toString(httpResponse.getEntity(), Consts.UTF_8);
    }

    /**
     * It finds the value inside the xml based on expression. if the expression
     * is invalid, it returns empty string.
     *
     * @param xml
     * @param expr
     * @return either the found value or an empty string
     * @throws ParserConfigurationException
     * @throws XPathExpressionException
     * @throws IOException
     * @throws SAXException
     */
    public static String extract(String xml, String expr)
            throws ParserConfigurationException, XPathExpressionException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document document = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        XPathExpression exp = XPathFactory.newInstance().newXPath().compile(expr);
        return (String) exp.evaluate(document, XPathConstants.STRING);
    }
}
