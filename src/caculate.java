import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class caculate {
    public static void main(String[] args)throws UnsupportedEncodingException {
//        String[] data=readAllFile("1.txt");

//        averageCallingTimes(data);

//        rateOfMobileCompy(data);

//        timeRate(data);

        String ss="fafwqqw";
        String[] aa=new String[1];
        aa[0]=ss;
        System.out.println(ss.getBytes("utf-8").length);
        System.out.println(aa.toString());

    }


    //计算平均通话次数
    public static void averageCallingTimes(String[] dataArr){
        HashMap<String,Integer> sets=new HashMap<>();
        for (int i=0;i<dataArr.length;i++) {
            String[] elements=dataArr[i].split("\\s+");
            if(sets.get(elements[1])==null){
                sets.put(elements[1],1);
            }else{
                sets.put(elements[1],sets.get(elements[1])+1);
            }
        }

//        System.out.println(sets.toString());
        try {
            FileWriter writer = new FileWriter("out1.txt",false);
            for(String key:sets.keySet())
            {
                writer.write("<"+key+","+(double)sets.get(key)/29+">\n");
            }
            writer.flush();//刷新内存，将内存中的数据立刻写出。
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //不同通话类型（市话、长途、国际）下各个运营商（电信，移动，联通）的占比
    public static void rateOfMobileCompy(String[] dataArr){
        //0：电信； 1：移动； 2：联通
        //0：市话； 1：长途； 2：漫游
        int[][] sets=new int[3][3];
        for (int i=0;i<dataArr.length;i++) {
            String[] elements=dataArr[i].split("\\s+");
            if(elements[4]!=null && elements[12]!=null){
//                if(!(elements[4].equals("1")||elements[4].equals("2")||elements[4].equals("3")))
//                    System.out.println(elements[4]);
                //默认初始为0
                sets[Integer.parseInt(elements[12])-1][Integer.parseInt(elements[4])-1]++;
            }
        }

        generateChart((double)sets[0][0],(double)sets[0][1],(double)sets[0][1],"市话");
        generateChart((double)sets[1][0],(double)sets[1][1],(double)sets[1][1],"长途");
        generateChart((double)sets[2][0],(double)sets[2][1],(double)sets[2][1],"国际");
    }

    public static void generateChart(Double v1,Double v2,Double v3,String imgName){
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Telecom", v1);
        dataset.setValue("Mobile", v2);
        dataset.setValue("Unicom", v3);

        JFreeChart chart = ChartFactory.createPieChart("pieChart", // chart
                dataset, // data
                true, // include legend
                true, false);
        setChart(chart);
        PiePlot pieplot = (PiePlot) chart.getPlot();
        pieplot.setSectionPaint("Telecom", Color.decode("#749f83"));
        pieplot.setSectionPaint("Mobile", Color.decode("#2f4554"));
        pieplot.setSectionPaint("Unicom", Color.decode("#61a0a8"));

        try {
//            // 创建图形显示面板
//            ChartFrame cf = new ChartFrame("柱状图", chart);
//            // cf.pack();
//            // // 设置图片大小
//            cf.setSize(600, 600);
//            // // 设置图形可见
//            cf.setVisible(true);
            // 保存图片到指定文件夹
            ChartUtilities.saveChartAsPNG(new File(imgName+".png"), chart, 1500, 800);
            System.err.println("成功");
        } catch (Exception e) {
            System.err.println("创建图形时出错");
        }
    }


    public static void setChart(JFreeChart chart) {
        chart.setTextAntiAlias(true);

        PiePlot pieplot = (PiePlot) chart.getPlot();
        // 设置图表背景颜色
        pieplot.setBackgroundPaint(ChartColor.WHITE);

        pieplot.setLabelBackgroundPaint(null);// 标签背景颜色

        pieplot.setLabelOutlinePaint(null);// 标签边框颜色

        pieplot.setLabelShadowPaint(null);// 标签阴影颜色

        pieplot.setOutlinePaint(null); // 设置绘图面板外边的填充颜色

        pieplot.setShadowPaint(null); // 设置绘图面板阴影的填充颜色

        pieplot.setSectionOutlinesVisible(false);

        pieplot.setNoDataMessage("没有可供使用的数据！");
    }


    //计算用户在各个时间段通话时长所占比例
    public static void timeRate(String[] dataArr){
        HashMap<String,HashMap<Integer,Integer>> sets=new HashMap<>();
        for (int i=0;i<dataArr.length;i++) {
            String[] elements=dataArr[i].split("\\s+");
            if(elements[1]!=null && elements[9]!=null){
                if(sets.get(elements[1])==null){
                    HashMap<Integer,Integer> set=new HashMap<>();
                    set.put(classifyTime(elements[9]),1);
                    sets.put(elements[1],set);
                }else{
                    HashMap<Integer,Integer> set=sets.get(elements[1]);
                    int timePart=classifyTime(elements[9]);
                    if(set.get(timePart)==null)
                        set.put(timePart,1);
                    else
                        set.put(timePart,set.get(timePart)+1);
                }
            }
        }
        System.out.println(sets.toString());
    }

//    时间段 1  0:00-3:00
//    时间段 2  3:00-6:00
//    时间段 3  6:00-9:00
//    时间段 4  9:00-12:00
//    时间段 5  12:00-15:00
//    时间段 6  15:00-18:00
//    时间段 7  18:00-21:00
//    时间段 8  21:00-24:00
    public static int classifyTime(String time){
        int hour=Integer.parseInt(time.substring(0,time.indexOf(":")));
        if(hour>=0&&hour<3)
            return 1;
        else if(hour>=3&&hour<6){
            return 2;
        }else if(hour>=6&&hour<9){
            return 3;
        }else if(hour>=9&&hour<12){
            return 4;
        }else if(hour>=12&&hour<15){
            return 5;
        }else if(hour>=15&&hour<18){
            return 6;
        }else if(hour>=18&&hour<21){
            return 7;
        }else if(hour>=21&&hour<24){
            return 8;
        }else
            return 0;
    }


    public static String[] readAllFile(String name){
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            FileReader fr = new FileReader(name);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
            }
            bf.close();
            fr.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        // 对ArrayList中存储的字符串进行处理
        int length = arrayList.size();
        String[] array = new String[length];
        for (int i = 0; i < length; i++) {
            String s = arrayList.get(i);
            array[i] = s;
        }
        // 返回数组
        return array;
    }
}
