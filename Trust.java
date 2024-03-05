package data;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author Li mg
 * @version 1.0
 * @date 2023/5/5
 */
public class Trust
{
    public static void main(String args[]) throws IOException {
        Trust trust = new Trust();
        //trust.File_write();
        //trust.distance_csv();
        double distance_exp = 0.021;
        Distance_2 d_threshold = trust.distance_threshold(distance_exp);
        //trust.trust_double(d_threshold);
    }

    /**
     * @throws IOException
     */
    public void File_write() throws IOException {
        //
        File file = new File("src/source/data.csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        //
        DecimalFormat decimalFormat = new DecimalFormat("0.000000000");
        //
        writer.write("device,order,ratio,message,delay");
        double ratio=0,message=0,delay=0;
        for(int i=8;i<11;i++)
        {
            for(int j=1;j<11;j++)
            {
                writer.newLine();
                writer.write(String.valueOf(i) +","+String.valueOf(j));
                Random random = new Random();
                ratio = random.nextDouble()/20 + 0.51;    //ratio: [0.51-0.56)
                message = random.nextDouble()/20 + 29.88; //message: [29.88,29.93)
                delay = random.nextDouble()/200 + 0.04;   //delay: [0.040-0.045)
                writer.write(","+String.valueOf(decimalFormat.format(ratio))
                                +","+String.valueOf(decimalFormat.format(message))
                                +","+String.valueOf(decimalFormat.format(delay)));
            }
        }
        writer.flush();
        writer.close();
    }

    /**
     * device_id,order_id
     * @return
     */
    public Distance_2 trust_0_1(int device_id, int order_id) throws IOException {
        int sum=100;
        Distance_2 distance2=new Distance_2();
        /**
         * 1
         */
        Device device[] = new Device[sum];
        for(int t=0;t<sum;t++) //
        {
            device[t] = new Device();
        }
        File file = new File("src/source/lab_data.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = "";
        int num=0;
        for(int t=0;t<100;t++)
        //while((line = reader.readLine())!=null)
        {
            line = reader.readLine();
            //
            int i=0;
            String temp[] = new String[5];
            for (String retval: line.split(",", 5)){
                temp[i]=retval;
                i++;
            }
            //System.out.println(Integer.valueOf(temp[0])+"  "+Integer.valueOf(temp[1])+"  "+Double.valueOf(temp[2])+"  "+temp[3]+"  "+temp[4]);

            //
            device[num].setDevice_id(Integer.valueOf(temp[0]));
            device[num].setData_order(Integer.valueOf(temp[1]));
            device[num].setRatio(Double.valueOf(temp[2]));
            device[num].setMessage(Double.valueOf(temp[3]));
            device[num].setDelay(Double.valueOf(temp[4]));
            num++;
        }
        //for(int i=0;i<100;i++)
        //{
            //System.out.println(device[i].getDevice_id()+"  "+device[i].getData_order()+"  "+device[i].getRatio()+"  "+device[i].getMessage()+"  "+device[i].getDelay());
        //}
        reader.close();

        /**
         * 2
         */
        double k = 4;

        //
        Device device_once[] = new Device[10];
        for(int i=0;i<10;i++)
        {
            device_once[i] = new Device();
        }

        Device device_fix[] = new Device[10];
        for(int i=0;i<10;i++)
        {
            device_fix[i] = new Device();
        }
        for(int i=0;i<10;i++)
        {
            device_once[i]=device[(device_id-1)*10+i];
            device_fix[i] = device_once[i];
            //System.out.println(device_once[i].getDevice_id()+"  "+device_once[i].getData_order());
        }

        //
        int flag =0;
        Device device_cen[]= new Device[9];
        for(int i=0;i<9;i++)
        {
            device_cen[i] = new Device();
        }

        for(int j=0;j<10;j++)    //
        {
            if((j+1)!=order_id)
            {
                for(int i=0;i<10;i++)           //
                {

                    double temp_dis = Math.sqrt(Math.pow((device_once[i].getMessage()-device_once[j].getMessage()),2)
                            +Math.pow((device_once[i].getRatio()-device_once[j].getRatio()),2)
                            +Math.pow((device_once[i].getDelay()-device_once[j].getDelay()),2));
                    device_once[i].setDis(temp_dis);

                }
                device_once[j].setDis(999);

                /*if(j==8)
                {
                    for(int i=0;i<10;i++)
                    {
                        System.out.println(i+"  "+device_once[i].getDevice_id()+"  "+device_once[i].getData_order()+"  "+device_once[i].getRatio()
                                +"  "+device_once[i].getMessage()+"  "+device_once[i].getDelay()+"  "+device_once[i].getDis());

                    }
                }*/

                for(int i=0;i<10;i++)           //
                {
                    for(int t=i+1;t<10;t++)
                    {
                        if(device_once[i].getDis()>device_once[t].getDis())
                        {
                            Device device_temp = device_once[i];
                            device_once[i]=device_once[t];
                            device_once[t]=device_temp;
                        }
                    }
                }

               /* if(j==8)
                {
                    for(int i=0;i<10;i++)
                    {
                        System.out.println(i+"  "+device_once[i].getDevice_id()+"  "+device_once[i].getData_order()+"  "+device_once[i].getRatio()
                                            +"  "+device_once[i].getMessage()+"  "+device_once[i].getDelay()+"  "+device_once[i].getDis());

                    }
                }*/

                double ratio_max=0;double message_max=0;double delay_max=0;           //
                double ratio_min=999;double message_min=999;double delay_min=999;
                for(int i=0;i<k;i++)
                {
                    if(device_once[i].getDelay()>delay_max) {    delay_max=device_once[i].getDelay();    }
                    if(device_once[i].getDelay()<delay_min){    delay_min=device_once[i].getDelay();    }
                    if(device_once[i].getRatio()>ratio_max){    ratio_max=device_once[i].getRatio();    }
                    if(device_once[i].getRatio()<ratio_min){   ratio_min=device_once[i].getRatio();  }
                    if(device_once[i].getMessage()>message_max){    message_max = device_once[i].getMessage();  }
                    if(device_once[i].getMessage()<message_min){   message_min = device_once[i].getMessage();  }
                }
                device_cen[flag].setRatio((ratio_max + ratio_min)/2);
                device_cen[flag].setMessage((message_max + message_min)/2);
                device_cen[flag].setDelay((delay_max + delay_min)/2);
                device_cen[flag].setData_order(j+1);
                device_cen[flag].setDevice_id(device_id);
                flag++;
            }
            for(int i=0;i<10;i++)
            {
                device_once[i]=device_fix[i];
                //System.out.println(device_once[i].getDevice_id()+"  "+device_once[i].getData_order());
            }
        }

        for(int i=0;i<9;i++)
        {
            //device_once[i]=device_fix[i];
            //System.out.println(device_cen[i].getDevice_id()+"  "+device_cen[i].getData_order());
        }

        //
        for(int i=0;i<9;i++)           //
        {

            double temp_dis = Math.sqrt(Math.pow((device_cen[i].getMessage()-device_fix[order_id-1].getMessage()),2)
                    +Math.pow((device_cen[i].getRatio()-device_fix[order_id-1].getRatio()),2)
                    +Math.pow((device_cen[i].getDelay()-device_fix[order_id-1].getDelay()),2));
            device_cen[i].setDis(temp_dis);

        }
        for(int i=0;i<9;i++)           //
        {
            for(int t=i+1;t<9;t++)
            {
                if(device_cen[i].getDis()>device_cen[t].getDis())
                {
                    Device device_temp = device_cen[i];
                    device_cen[i]=device_cen[t];
                    device_cen[t]=device_temp;
                }
            }
        }

        for(int i=0;i<9;i++)
        {
            // System.out.println(device_cen[i].getDevice_id()+"  "+device_cen[i].getData_order()+"  "+device_cen[i].getRatio()+"  "+device_cen[i].getMessage()+"  "+device_cen[i].getDelay()+"  "+device_cen[i].getDis());
        }

        double ratio_max=0;double message_max=0;double delay_max=0;           //
        double ratio_min=999;double message_min=999;double delay_min=999;
        for(int i=0;i<k;i++)
        {
            //System.out.println(i+"  "+device_cen[i].getData_order());
            if(device_cen[i].getDelay()>delay_max) {    delay_max=device_cen[i].getDelay();    }
            if(device_cen[i].getDelay()<delay_min){    delay_min=device_cen[i].getDelay();    }
            if(device_cen[i].getRatio()>ratio_max){    ratio_max=device_cen[i].getRatio();    }
            if(device_cen[i].getRatio()<ratio_min){   ratio_min=device_cen[i].getRatio();  }
            if(device_cen[i].getMessage()>message_max){    message_max = device_cen[i].getMessage();  }
            if(device_cen[i].getMessage()<message_min){   message_min = device_cen[i].getMessage();  }
        }
        double ratio_cen=(ratio_max + ratio_min)/2;
        double message_cen=((message_max + message_min)/2);
        double delay_cen=((delay_max + delay_min)/2);

        distance2.zong =  Math.sqrt(Math.pow((message_cen-device_once[order_id-1].getMessage()),2)
                +Math.pow((ratio_cen-device_once[order_id-1].getRatio()),2)
                +Math.pow((delay_cen-device_once[order_id-1].getDelay()),2));


        /**
         * 3
         */

        for(int i=0;i<10;i++)
        {
            device_once[i] = new Device();
        }
        for(int i=0;i<10;i++)
        {
            device_fix[i] = new Device();
        }
        for(int i=0;i<10;i++)
        {
            device_once[i]=device[order_id-1+i*10];
            device_fix[i] = device_once[i];
            //System.out.println(device_once[i].getDevice_id()+"  "+device_once[i].getData_order());
        }

        //
        flag =0;

        for(int i=0;i<9;i++)
        {
            device_cen[i] = new Device();
        }

        for(int j=0;j<10;j++)    //
        {
            if((j+1)!=device_id)
            {
                for(int i=0;i<10;i++)           //
                {

                    double temp_dis = Math.sqrt(Math.pow((device_once[i].getMessage()-device_once[j].getMessage()),2)
                            +Math.pow((device_once[i].getRatio()-device_once[j].getRatio()),2)
                            +Math.pow((device_once[i].getDelay()-device_once[j].getDelay()),2));
                    device_once[i].setDis(temp_dis);

                }
                device_once[j].setDis(999);

              /*  if(j==8)
                {
                    for(int i=0;i<10;i++)
                    {
                        System.out.println(i+"  "+device_once[i].getDevice_id()+"  "+device_once[i].getData_order()+"  "+device_once[i].getRatio()
                                +"  "+device_once[i].getMessage()+"  "+device_once[i].getDelay()+"  "+device_once[i].getDis());

                    }
                }*/

                for(int i=0;i<10;i++)           //
                {
                    for(int t=i+1;t<10;t++)
                    {
                        if(device_once[i].getDis()>device_once[t].getDis())
                        {
                            Device device_temp = device_once[i];
                            device_once[i]=device_once[t];
                            device_once[t]=device_temp;
                        }
                    }
                }

               /* if(j==8)
                {
                    for(int i=0;i<10;i++)
                    {
                        System.out.println(i+"  "+device_once[i].getDevice_id()+"  "+device_once[i].getData_order()+"  "+device_once[i].getRatio()
                                            +"  "+device_once[i].getMessage()+"  "+device_once[i].getDelay()+"  "+device_once[i].getDis());

                    }
                }*/

                ratio_max=0;message_max=0;delay_max=0;           //
                ratio_min=999;message_min=999;delay_min=999;
                for(int i=0;i<k;i++)
                {
                    if(device_once[i].getDelay()>delay_max) {    delay_max=device_once[i].getDelay();    }
                    if(device_once[i].getDelay()<delay_min){    delay_min=device_once[i].getDelay();    }
                    if(device_once[i].getRatio()>ratio_max){    ratio_max=device_once[i].getRatio();    }
                    if(device_once[i].getRatio()<ratio_min){   ratio_min=device_once[i].getRatio();  }
                    if(device_once[i].getMessage()>message_max){    message_max = device_once[i].getMessage();  }
                    if(device_once[i].getMessage()<message_min){   message_min = device_once[i].getMessage();  }
                }
                device_cen[flag].setRatio((ratio_max + ratio_min)/2);
                device_cen[flag].setMessage((message_max + message_min)/2);
                device_cen[flag].setDelay((delay_max + delay_min)/2);
                device_cen[flag].setData_order(j+1);
                device_cen[flag].setDevice_id(device_id);
                flag++;
            }
            for(int i=0;i<10;i++)
            {
                device_once[i]=device_fix[i];
                //System.out.println(device_once[i].getDevice_id()+"  "+device_once[i].getData_order());
            }
        }

        for(int i=0;i<9;i++)
        {
            //device_once[i]=device_fix[i];
            //System.out.println(device_cen[i].getDevice_id()+"  "+device_cen[i].getData_order());
        }

        //
        for(int i=0;i<9;i++)           //
        {

            double temp_dis = Math.sqrt(Math.pow((device_cen[i].getMessage()-device_fix[device_id-1].getMessage()),2)
                    +Math.pow((device_cen[i].getRatio()-device_fix[device_id-1].getRatio()),2)
                    +Math.pow((device_cen[i].getDelay()-device_fix[device_id-1].getDelay()),2));
            device_cen[i].setDis(temp_dis);

        }
        for(int i=0;i<9;i++)           //
        {
            for(int t=i+1;t<9;t++)
            {
                if(device_cen[i].getDis()>device_cen[t].getDis())
                {
                    Device device_temp = device_cen[i];
                    device_cen[i]=device_cen[t];
                    device_cen[t]=device_temp;
                }
            }
        }

        for(int i=0;i<9;i++)
        {
            // System.out.println(device_cen[i].getDevice_id()+"  "+device_cen[i].getData_order()+"  "+device_cen[i].getRatio()+"  "+device_cen[i].getMessage()+"  "+device_cen[i].getDelay()+"  "+device_cen[i].getDis());
        }

        ratio_max=0;message_max=0;delay_max=0;           //
        ratio_min=999;message_min=999;delay_min=999;
        for(int i=0;i<k;i++)
        {
            //System.out.println(i+"  "+device_cen[i].getData_order());
            if(device_cen[i].getDelay()>delay_max) {    delay_max=device_cen[i].getDelay();    }
            if(device_cen[i].getDelay()<delay_min){    delay_min=device_cen[i].getDelay();    }
            if(device_cen[i].getRatio()>ratio_max){    ratio_max=device_cen[i].getRatio();    }
            if(device_cen[i].getRatio()<ratio_min){   ratio_min=device_cen[i].getRatio();  }
            if(device_cen[i].getMessage()>message_max){    message_max = device_cen[i].getMessage();  }
            if(device_cen[i].getMessage()<message_min){   message_min = device_cen[i].getMessage();  }
        }
        ratio_cen=(ratio_max + ratio_min)/2;
        message_cen=((message_max + message_min)/2);
        delay_cen=((delay_max + delay_min)/2);

        distance2.heng =  Math.sqrt(Math.pow((message_cen-device_once[device_id-1].getMessage()),2)
                +Math.pow((ratio_cen-device_once[device_id-1].getRatio()),2)
                +Math.pow((delay_cen-device_once[device_id-1].getDelay()),2));

        return distance2;
    }

    /**
     *
     * @throws IOException
     */
    public void distance_csv() throws IOException {
        Trust trust = new Trust();
        Distance_2 distance_2[] = new Distance_2[100];
        for(int i=0;i<100;i++)
        {
            distance_2[i] = new Distance_2();
        }

        int flag =0;
        for(int i=1;i<11;i++)
        {
            for(int j=1;j<11;j++)
            {
                distance_2[flag] = trust.trust_0_1(i,j);
                System.out.println(i+"  "+j+"  "+flag+"  "+distance_2[flag].heng+"  "+distance_2[flag].zong);
                flag++;
            }
        }

        File file = new File("src/source/distance.csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        //
        DecimalFormat decimalFormat = new DecimalFormat("0.000000000");
        //
        writer.write("heng,zong");
        for(int i=0;i<100;i++)
        {
            writer.newLine();
            writer.write(decimalFormat.format(distance_2[i].heng)+","+decimalFormat.format(distance_2[i].zong));
        }

        writer.flush();
        writer.close();
    }

    /**
     * @return
     */
    public Distance_2 distance_threshold(double distance_exp) throws IOException {
        File file = new File("src/source/distance.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = "";
        line = reader.readLine();
        double heng[] = new double[100];
        double zong[] = new double[100];
        for(int num=0;num<100;num++)
        {
            line = reader.readLine();
            int i=0;
            String temp[] = new String[2];
            for (String retval: line.split(",", 2)){
                temp[i]=retval;
                i++;
            }
            heng[num] = Double.valueOf(temp[0]);
            zong[num] = Double.valueOf(temp[1]);
        }
        for(int i=0;i<100;i++)
        {
            for(int j=i+1;j<100;j++)
            {
                if(heng[i]>heng[j])
                {
                    double temp_double = heng[i];
                    heng[i] = heng[j];
                    heng[j] = temp_double;
                }
                if(zong[i]>zong[j])
                {
                    double temp_double = zong[i];
                    zong[i] = zong[j];
                    zong[j] = temp_double;
                }
            }
        }
        System.out.println(heng[76]+"  "+zong[76]);//heng:0.038+  ,zong:0.037+
        Distance_2 d = new Distance_2();
        d.heng = (heng[49]+heng[50])/2 + distance_exp;
        d.zong = (zong[49]+zong[50])/2 + distance_exp;

        //
        double threshold_1=0,threshold_2=0;
        for(int i=0;i<100;i++)
        {
            threshold_1+=Math.sqrt(heng[i]);
            threshold_2+=Math.sqrt(zong[i]);
        }
        System.out.println(threshold_1/100+threshold_2/100);

        //
        double ave_1=0,ave_2=0,fa_1=0,fa_2=0;
        for(int i=0;i<100;i++)
        {
            ave_1+=heng[i];
            ave_2+=zong[i];
        }
        double a_1 = ave_1/100;
        double a_2 = ave_2/100;
        for(int i=0;i<100;i++)
        {
            fa_1+=Math.pow((a_1-heng[i]),2);
            fa_2+=Math.pow((a_2-zong[i]),2);
        }
        fa_1 = Math.sqrt(fa_1/100);
        fa_2 = Math.sqrt(fa_2/100);
        System.out.println(+a_1+"  "+a_2+"  "+fa_1/(a_1-heng[2])+"  "+fa_2);
        System.out.println(((heng[49]+heng[50])/2)*2);
        return d;

    }

    /**
     *
     */
    public void trust_double(Distance_2 threshold) throws IOException
    {
        /**
         *  //
         */
        File file = new File("src/source/distance.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = "";
        line = reader.readLine();
        double heng[] = new double[100];
        double zong[] = new double[100];
        for(int num=0;num<100;num++)
        {
            line = reader.readLine();
            int i=0;
            String temp[] = new String[2];
            for (String retval: line.split(",", 2)){
                temp[i]=retval;
                i++;
            }
            heng[num] = Double.valueOf(temp[0]);
            zong[num] = Double.valueOf(temp[1]);
        }
        reader.close();
        DecimalFormat decimalFormat = new DecimalFormat("0.0000000");

        /**
         * //
         */
        file = new File("src/source/trust_1_2.csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("trust1,trust2");
        for(int i=0;i<100;i++)
        {
            String t1_s = decimalFormat.format(1/(1+Math.exp(heng[i]-threshold.heng)));
            double t1 = Double.valueOf(t1_s);
            String t2_s = decimalFormat.format(1/(1+Math.exp(zong[i]-threshold.zong)));
            double t2 = Double.valueOf(t2_s);
            //System.out.println(i+"  "+t1_s+"  "+t2_s);
            writer.newLine();
            writer.write(t1_s+","+t2_s);
        }
        writer.flush();
        writer.close();

        /**
         * //
         */
        file = new File("src/source/trust_1_2.csv");
        reader = new BufferedReader(new FileReader(file));
        line = reader.readLine();
        double trust_1[] = new double[100];
        double trust_2[] = new double[100];
        for(int num=0;num<100;num++)
        {
            line = reader.readLine();
            int i=0;
            String temp[] = new String[2];
            for (String retval: line.split(",", 2)){
                temp[i]=retval;
                i++;
            }
            trust_1[num] = Double.valueOf(temp[0]);
            trust_2[num] = Double.valueOf(temp[1]);
        }
        reader.close();

        file = new File("src/source/trust.csv");
        writer = new BufferedWriter(new FileWriter(file));
        writer.write("trust");
        for(int i=0;i<100;i++)
        {
            double temp = (trust_1[i]+trust_2[i])/2;
            writer.newLine();
            writer.write(String.valueOf(temp));
        }
        writer.flush();
        writer.close();

    }


}
