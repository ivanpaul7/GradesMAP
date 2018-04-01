package lab.repository.XML;

import javafx.util.Pair;
import lab.domain.Grade;
import lab.domain.Grade2;
import lab.domain.Homework;
import lab.domain.Student;
import lab.repository.GenericRepository;
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

public class GradeXMLRepository extends AbstractXMLRepository<Pair<Student,Homework>, Grade2> {
    GenericRepository<Integer, Student> studRepo;
    GenericRepository<Integer, Homework> hmkRepo;
    public GradeXMLRepository(String numeFis, Validator<Grade2> val, GenericRepository<Integer, Student> studRepo, GenericRepository<Integer, Homework> hmkRepo){
        super(numeFis,val);
        this.hmkRepo=hmkRepo;
        this.studRepo=studRepo;
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
            for(Grade2 gr: getAll()){
                // student element
                Element grade = doc.createElement("grade");
                rootElement.appendChild(grade);

                Element idStudent = doc.createElement("idStudent");
                idStudent.appendChild(doc.createTextNode(""+gr.getId().getKey().getId()));
                grade.appendChild(idStudent);

                Element idHomework = doc.createElement("idHomework");
                idHomework.appendChild(doc.createTextNode(""+gr.getId().getValue().getId()));
                grade.appendChild(idHomework);

                Element value = doc.createElement("value");
                value.appendChild(doc.createTextNode(""+gr.getValue()));
                grade.appendChild(value);
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
            NodeList nList = doc.getElementsByTagName("grade");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    super.add(
                            new Grade2(
                                    new Pair<Student, Homework>(studRepo.get( Integer.parseInt( eElement.getElementsByTagName("idStudent").item(0).getTextContent())),
                                                                hmkRepo.get(  Integer.parseInt( eElement.getElementsByTagName("idHomework").item(0).getTextContent()))),
                                    Integer.parseInt( eElement.getElementsByTagName("value").item(0).getTextContent())
                            )
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
