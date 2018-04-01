package lab.repository.XML;

import lab.domain.Student;
import lab.validator.Validator;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import javax.xml.transform.OutputKeys;

public class StudentXMLRepository  extends AbstractXMLRepository<Integer, Student> {
    public StudentXMLRepository(String numeFis, Validator<Student> val){
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
            for(Student st: getAll()){
                // student element
                Element student = doc.createElement("student");
                rootElement.appendChild(student);

                // setting attribute to element
                Attr attr = doc.createAttribute("id");
                attr.setValue(""+st.getId());
                student.setAttributeNode(attr);

                // name element
                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(st.getName()));
                student.appendChild(name);

                Element group = doc.createElement("group");
                group.appendChild(doc.createTextNode(""+st.getGroup()));
                student.appendChild(group);

                Element email = doc.createElement("email");
                email.appendChild(doc.createTextNode(st.getEmail()));
                student.appendChild(email);

                Element professor = doc.createElement("professor");
                professor.appendChild(doc.createTextNode(st.getProfessor()));
                student.appendChild(professor);

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
            // Output to console for testing
//            StreamResult consoleResult = new StreamResult(System.out);
//            transformer.transform(source, consoleResult);
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
            NodeList nList = doc.getElementsByTagName("student");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    super.add(
                            new Student(
                                    Integer.parseInt( eElement.getAttribute("id")),
                                    eElement.getElementsByTagName("name").item(0).getTextContent(),
                                    Integer.parseInt(eElement.getElementsByTagName("group").item(0).getTextContent()),
                                    eElement.getElementsByTagName("email").item(0).getTextContent(),
                                    eElement.getElementsByTagName("professor").item(0).getTextContent()
                            )
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
