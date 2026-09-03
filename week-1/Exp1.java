public class Exp1{
    static void checkDup(int[] seatNo){
        boolean[] alrReported =new boolean[seatNo.length];
        boolean dupFound=false;

        for (int i=0;i<seatNo.length;i++){
            if(alrReported[i]) continue;
            for (int j=i+1;j<seatNo.length;j++){
                if (seatNo[i]==seatNo[j]){
                    System.out.println("Seats are repeated"+seatNo[i]);
                    dupFound=true;
                    alrReported[i]=true;
                    alrReported[j]=true;
                }
            }
        }
        if(!dupFound){
            System.out.println("Seats are not repeated");
        }
    }

    public static void main(String[] args){
        int[] test1 = {101, 102, 103, 102, 105};
        int[] test2 = {101, 102, 103, 104, 105};
        System.out.println("Test 1:");
        checkDup(test1);
        System.out.println("\nTest 2:");
        checkDup(test2);
    }
}