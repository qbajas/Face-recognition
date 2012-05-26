package data;

/**
 * Zawiera tablice wyjsc oczekiwanych z sieci dla poszczegolnych twarzy.
 * @author Michal
 */
public class OutputContainer {
    double [][]outputs;
    
    public OutputContainer(int subjectCount){
         outputs = new double[subjectCount+1][];
         double output[] = new double[subjectCount];
         outputs[0] = output;
         for(int i=1;i<outputs.length;++i){
             output = new double[subjectCount];
             output[i-1] = 1;
             outputs[i] = output;
         }
    }
    
    public double[] getIdealOutput(int subjectNbr){
        if(subjectNbr<0 || subjectNbr>outputs.length)
            return outputs[0]; //wyjscie dla twarzy nierozpoznanej
        return outputs[subjectNbr];
    }
}
