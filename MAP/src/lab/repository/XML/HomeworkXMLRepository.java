package lab.repository.XML;

import lab.domain.Homework;
import lab.domain.Student;
import lab.validator.Validator;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class HomeworkXMLRepository extends AbstractXMLRepository<Integer, Homework> {
    public HomeworkXMLRepository(String numeFis, Validator<Homework> val){
        super(numeFis,val);
        loading();
    }

    @Override
    protected void writeToFile() {
        try {
            DocumentBuilderFactory dbFactory =DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("class");
            doc.appendChild(rootElement);
            for(Homework hmk: getAll()){
                // student element
                Element homework = doc.createElement("homework");
                rootElement.appendChild(homework);

                // setting attribute to element
                Attr attr = doc.createAttribute("id");
                attr.setValue(""+hmk.getId());
                homework.setAttributeNode(attr);

                // name element
                Element description = doc.createElement("description");
                description.appendChild(doc.createTextNode(hmk.getDescription()));
                homework.appendChild(description);

                Element deadline = doc.createElement("deadline");
                deadline.appendChild(doc.createTextNode(""+hmk.getDeadline()));
                homework.appendChild(deadline);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "5");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(numeFis));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loading() {
        try {
            File inputFile = new File(numeFis);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("homework");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    super.add(
                            new Homework(
                                    Integer.parseInt( eElement.getAttribute("id")),
                                    eElement.getElementsByTagName("description").item(0).getTextContent(),
                                    Integer.parseInt(eElement.getElementsByTagName("deadline").item(0).getTextContent())
                            )
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
