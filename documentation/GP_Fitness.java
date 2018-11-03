import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GP_Fitness {

    private AST AST_tree;
//    private

    public GP_Fitness() {
//        AST_tree = new AST<...>();
    }
//Patches here is a class that contains List (or ArrayList) of Insertion, Deletion, Replacement
    public ArrayList<Patches> Loop_population(ArrayList<Patches> population){


        ArrayList<AST> modifiedAST = new ArrayList<AST>();
        for (Patches patch : population) {
//            Use your function
//            I'm not sure here which data_type you need, you can modify to satisfy your choice of data type
//            maybe you can convert from xml to AST again, or maybe I can do it for you
//  convert_to_XML : change type Patches -> XML
            XML xml_data = convert_to_XML(patch);
//            modify AST by those insert, replace, delete
//  modify_AST : insert,replace,delete xml data type -> AST
            modifiedAST.add(modify_AST(xml_data));
        }

//  now I will convert it to JAVA and compile it
        for (AST each_modified_AST : modifiedAST){
//  Maybe we need True/False and performance or anything you need in that Java_Result Class
            JavaResult result = Java_compile(each_modified_AST);

        }
        return allAST;
    }
    public JavaResult Java_compile(AST modified_AST){
        JavaResult result = new JavaResult<>();
//        run the java file


//        use fitness function somehow from JavaResult

        return result;
    }
}
