package lab.service;



import javafx.util.Pair;
import lab.domain.*;
import lab.repository.GenericRepository;
import lab.repository.RepositoryException;
import lab.repository.statistics.StatisticsRepository;
import lab.utils.ListEvent;
import lab.utils.ListEventType;
import lab.validator.ServiceException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Service extends Observable{
    private GenericRepository<Integer, Student> stdRepo;
    private GenericRepository<Integer, Homework> hmkRepo;
    private GenericRepository<Pair<Student, Homework>, Grade2> gradesRepo;
    private GenericRepository<String, Password> passwordRepo;
    private StatisticsRepository statisticsRepository;
//    private ArrayList<Observer<Student>> studentObservers=new ArrayList<>();
//    private ArrayList<Observer<Grade2>> gradeObservers=new ArrayList<>();
//    private ArrayList<Observer<Homework>> homeworkObservers=new ArrayList<>();

    public Service(GenericRepository<Integer, Student> studentRepo, GenericRepository<Integer, Homework> hmkRepo, GenericRepository<Pair<Student, Homework>, Grade2> gradesRepo, GenericRepository<String, Password> passwRepo, StatisticsRepository statisticsRepository) {
        this.stdRepo = studentRepo;
        this.hmkRepo = hmkRepo;
        this.gradesRepo=gradesRepo;
        this.passwordRepo=passwRepo;
        this.statisticsRepository=statisticsRepository;
    }
    private <E> ListEvent<E> createEvent(ListEventType type, final E elem, final Iterable<E> l){
        return new ListEvent<E>(type) {
            @Override
            public Iterable<E> getList() {
                return l;
            }
            @Override
            public E getElement() {
                return elem;
            }
        };
    }
    public int checkValidUser(String user, String psw){
        try {
            Password psww=passwordRepo.get(user);
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(psw.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);

            if (psww.getPassword().equals(bigInt.toString())  ){
                if(psww.getUserType().equals("usualUser"))
                    return 1;
                if(psww.getUserType().equals("fullUser"))
                    return 2;
                if(psww.getUserType().equals("adminUser"))
                    return 3;
            }
        }catch(RepositoryException rr){
            //
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int weekOfYear= Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)-39;

    public void addStudent(Student st) {
        stdRepo.add(st);
        setChanged();
        notifyObservers();
    }

    public Student removeStudent(int id) {
        Student st = stdRepo.delete(id);
        List<Pair<Student,Homework>> listId= new ArrayList();
        for(Grade2 gr: gradesRepo.getAll())
                if (gr.getId().getKey().getId() == id)
                    listId.add(gr.getId());
        for(Pair<Student,Homework> pairId:listId)
            gradesRepo.delete(pairId);
        setChanged();
        notifyObservers();
        return st;
    }

    public void updateStudent(Student st){
        stdRepo.update(st.getId(), st);
        setChanged();
        notifyObservers();
    }

    public Student getStudent(int id){
        return stdRepo.get(id);
    }

    public void addTema(Homework hm) {
        hmkRepo.add(hm);
        setChanged();
        notifyObservers();
    }

    public void updateDeadline(int id, int newDeadline) {
        Homework homework = new Homework((Homework)(hmkRepo.get(id)));
        if(homework.getDeadline()<newDeadline && weekOfYear< homework.getDeadline() ) {
            homework.setDeadline(newDeadline);
            hmkRepo.update(id, homework);
            setChanged();
            notifyObservers();
        }else{
            throw new ServiceException(("ERR! deadline violation - overflow"));
        }
    }

    private Pair<Student, Homework> getIdExistentGrade(int idS, int idH){
        for(Grade2 gr:gradesRepo.getAll())
            if(gr.getId().getValue().getId()==idH && gr.getId().getKey().getId()==idS)
                return gr.getId();
        throw new ServiceException(("nota inexistenta!"));
    }

    private void testExistentStudent(int idS){ stdRepo.get(idS); }
    private void testExistentHomework(int idH){
        hmkRepo.get(idH);
    }

    private void fisaStudent(GradeDTO2 gr, boolean adaugare){
        String numeFis="src\\lab\\docs\\reports\\"+gr.getGrade().getId().getKey().getId()+".txt";
        try(PrintWriter pw= new PrintWriter(new FileWriter(numeFis,true))){
            String line;
            if(adaugare==true){
                line="Adaugare nota    "+gr.toString();
            }else{
                line="Modificare nota  "+gr.toString();
            }
            pw.println(line);
        }catch(IOException e){
            System.err.println(e);
        }
    }

    public Grade2 getGrade(Pair<Student,Homework> pair){
        return gradesRepo.get(pair);
    }

    private int calculateGradeValue(int val, int deadline){
        if(weekOfYear > deadline+2)
            return 0;
        else if(weekOfYear==deadline+1)
            return val-2;
        else if(weekOfYear==deadline+2)
            return val-4;
        return val;
    }
    private void validateGradesValue(int val){
        if(val<0 || val>10)
            throw new ServiceException(("ERR! grade's value must be between 0 and 10"));
    }

    public void addGrade(int idS, int idH, int val, String observation){
        validateGradesValue(val);
        Student st= stdRepo.get(idS); Homework hm=hmkRepo.get(idH);  //verific daca exista studentul si tema cu id-urile primite
        val=calculateGradeValue(val, hm.getDeadline());
        //verific daca nu s-a dat deja o nota
        boolean existaDarNuSeModifica=false;
        try{
            Pair<Student,Homework> idG=getIdExistentGrade(idS,idH);
            // daca exita, modificam valoarea doar daca e mai mare decat val initiala
            Grade2 gr=gradesRepo.get(idG);
            int oldValue=gr.getValue();
            if(gr.getValue()<val) {
                gradesRepo.update(gr.getId(), new Grade2( gr.getId() , val));
                setChanged();
                notifyObservers();
                if(observation!="NULL"){
                    GradeDTO2 grDTO = new GradeDTO2(gr, weekOfYear, observation);
                    if(oldValue!=0){    // daca nu era null initial
                        fisaStudent(grDTO, false);
                    }else{
                        fisaStudent(grDTO, true);
                    }
                }
            }else{
                existaDarNuSeModifica=true;
            }
        }catch (ServiceException e){
            // daca nu exista o adaugam acum
            Grade2 gr= new Grade2(new Pair<Student,Homework>(st,hm), val);
            gradesRepo.add(gr);
            setChanged();
            notifyObservers();
            if(observation!="NULL"){
                GradeDTO2 grDTO = new GradeDTO2(gr, weekOfYear, observation);
                fisaStudent(grDTO, false);
            }
        }

        if(existaDarNuSeModifica)
            throw new ServiceException(("OBS! new value < old value (it ramains unchanged)"));
    }

    public Iterable<Student> getAllStudents() {
        return stdRepo.getAll();
    }

    public Iterable<Homework> getAllHomeworks() {
        return hmkRepo.getAll();
    }

    public Iterable<Grade2> getAllGrades(){return  gradesRepo.getAll();}

    private void generateNullGrades(){
        for(Student st: stdRepo.getAll()){
            for(Homework hmk:hmkRepo.getAll()){
                boolean exist=false;
                for(Grade2 gr:gradesRepo.getAll()){
                    if(gr.getId().getKey().getId()==st.getId() && gr.getId().getValue().getId()==hmk.getId())
                        exist=true;
                }
                if(!exist){
                    addGrade(st.getId(), hmk.getId(), 0, "NULL");
                }


            }
        }
    }

    public <E> List<E> iterableToList(Iterable<E> iterable){
        List<E> list=new ArrayList<E>();
        for(E elem:iterable){
            list.add(elem);
        }
        return list;
    }

    private <E> List<E> filter(List<E> list, Predicate<E> pred){
        List<E> ret=new ArrayList<E>();
        for(E elem:list){
            if(pred.test(elem))
                ret.add(elem);
        }
        return ret;
    }

    public <E>List<E> filterAndSorter(List<E> list, Predicate<E> pred, Comparator<E> comp) {
        return list.stream().filter(pred).sorted(comp).collect(Collectors.toList());
    }


    public static <K, V extends Comparable<? super V>> Map<K, V> sortMAPByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(/*Collections.reverseOrder()*/))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortMAPByValueReverse(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }


    public Map<Integer, Integer> nrHmkPerStudent(){
        Map<Integer, Integer> vectorAparitii=new HashMap<Integer, Integer>();
        for(Grade2 gr:getAllGrades()){
            if (vectorAparitii.get(gr.getId().getKey().getId() ) == null) {
                vectorAparitii.put(gr.getId().getKey().getId(), 1);
            } else {
                vectorAparitii.put(gr.getId().getKey().getId(), vectorAparitii.get(gr.getId().getKey().getId()) + 1);
            }
        }
        for(Student st: getAllStudents()){
            if (vectorAparitii.get(st.getId() ) == null) {
                vectorAparitii.put(st.getId(), 0);
            }
        }

        return vectorAparitii;
    }


    public Map<Integer, Integer> averageGrade(){
        long nrHmks = getAllHomeworks().spliterator().getExactSizeIfKnown();

        Map<Integer, Integer> vectorAparitii=new HashMap<Integer, Integer>();
        for(Grade2 gr:getAllGrades()){
            if (vectorAparitii.get(gr.getId().getKey().getId() ) == null) {
                vectorAparitii.put(gr.getId().getKey().getId(), gr.getValue());
            } else {
                vectorAparitii.put(gr.getId().getKey().getId(), vectorAparitii.get(gr.getId().getKey().getId()) + gr.getValue());
            }
        }

        for(Student st: getAllStudents()){
            if (vectorAparitii.get(st.getId() ) == null) {
                vectorAparitii.put(st.getId(), 0);
            } else {
                vectorAparitii.put(st.getId(), (int) (vectorAparitii.get(st.getId())/nrHmks));
            }
        }
//        for (Map.Entry<Integer, Integer> entry : vectorAparitii.entrySet()) {
//            System.out.println(entry.getKey() + ", " + entry.getValue());
//        }
        return vectorAparitii;
    }




//    public void saveToPdf(String numeFisier, List<StudentHOF> list) throws IOException {
//        statisticsRepository.saveToPDF(numeFisier, list);
//    }

//    public List<Student> filterByStudGroup(int group){
//        Predicate<Student> pred=x->x.getGroup()==group;
//        return filterAndSorter(iterableToList(stdRepo.getAll()), pred, compStudentId);
//    }
//    public List<Student> filterByStudName(String name){
//        Predicate<Student> pred=x->x.getName().contains(name);
//        return filterAndSorter(iterableToList(stdRepo.getAll()), pred, compStudentId);
////        List<Student> students=iterableToList(stdRepo.getAll());
////        students.stream().filter(x->x.getName().contains("Paul")).map(x->x.toString()).forEach(System.out::println);
////        return students;
//    }
//    public List<Student> filterByStudEmailDomain(String emailD){
//        Predicate<Student> pred=x->x.getEmail().endsWith(emailD);
//        return filterAndSorter(iterableToList(stdRepo.getAll()), pred, compStudentId );
//    }
//    public List<Homework> filterByHmkDescription(String descrp){
//        Predicate<Homework> pred=x->x.getDescription().contains(descrp);
//        return filterAndSorter(iterableToList(hmkRepo.getAll()), pred, cmpHomeworkId);
//    }
//    public List<Homework> filterByHmkDeadline(int deadline){
//        Predicate<Homework> pred=x->x.getDeadline()==deadline;
//        return filterAndSorter(iterableToList(hmkRepo.getAll()), pred, cmpHomeworkId);
//    }
//    public List<Homework> filterByHmkDeadlineOutdated(int deadline){
//        Predicate<Homework> pred=x->x.getDeadline()<=deadline;
//        return filterAndSorter(iterableToList(hmkRepo.getAll()), pred, cmpHomeworkId);
//    }
//    public List<Grade> filterByGradeValue(int val){
//        Predicate<Grade> pred=x->x.getValue()==val;
//        return filterAndSorter(iterableToList(gradesRepo.getAll()),pred, cmpGradeValue);
//    }
//    public List<Grade> filterByGradeValueLess(int val, boolean less){
//        Predicate<Grade> pred;
//        if(less==true)
//            pred=x->x.getValue()<=val;
//        else
//            pred=x->x.getValue()>=val;
//        return filterAndSorter(iterableToList(gradesRepo.getAll()),pred, cmpGradeValue);
//    }
//    public List<Grade> filterByGradeHmk(int idHmk){
//        Predicate<Grade> pred=x->x.getIdHomework()==idHmk;
//        return filterAndSorter(iterableToList(gradesRepo.getAll()),pred, cmpGradeId);
//    }
//    public List<Grade> filterByGradeStd(int idStd){
//        Predicate<Grade> pred=x->x.getIdStudent()==idStd;
//        return filterAndSorter(iterableToList(gradesRepo.getAll()),pred, cmpGradeId);
//    }


//    Comparator<Student> compStudentName=(x, y)->{ return x.getName().compareTo(y.getName()); };
//    Comparator<Student> compStudentId=(x,y)->{
//        if(x.getId()==y.getId())
//            return 0;
//        else if(x.getId()<y.getId())
//            return -1;
//        return 1;
//    };
//    Comparator<Homework> cmpHomeworkDeadline=(x,y)->{
//        if(x.getDeadline()==y.getDeadline())
//            return 0;
//        else if(x.getDeadline()<y.getDeadline())
//            return -1;
//        return 1;
//    };
//    Comparator<Homework> cmpHomeworkId=(x,y)->{
//        if(x.getId()==y.getId())
//            return 0;
//        else if(x.getId()<y.getId())
//            return -1;
//        return 1;
//    };
//    Comparator<Homework> cmpHomeworkDescription=(x, y)->{ return x.getDescription().compareTo(y.getDescription());};
//    Comparator<Grade> cmpGradeId=(x,y)->{
//        if(x.getId()==y.getId())
//            return 0;
//        else if(x.getId()<y.getId())
//            return -1;
//        return 1;
//    };
//    Comparator<Grade> cmpGradeValue=(x,y)->{
//        if(x.getValue()==y.getValue())
//            return 0;
//        else if(x.getValue()<y.getValue())
//            return -1;
//        return 1;
//    };
}
