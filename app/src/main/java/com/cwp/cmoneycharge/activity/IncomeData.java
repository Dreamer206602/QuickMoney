package com.cwp.cmoneycharge.activity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.ItypeDAO;
import cwp.moneycharge.model.KindData;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cwp.cmoneycharge.R;
import com.cwp.cmoneycharge.app.SysApplication;

public class IncomeData  extends Activity {
	int userid;
	Intent intentr;
	IncomeDAO incomeDAO;
	int defaultMonth;
	int defaultYear; 
	Time time ;//��ȡ��ǰʱ��
	LinearLayout piechart,idataselect;
	Button beforet,aftert,anytime;
	Spinner year,month,day,yeare,monthe,daye;//�����ϵ�����ʱ��
	List<String> yearlist;
	Adapter adapter;
	String date1,date2;
	List<KindData> KindDatai;
	DefaultRenderer mRenderer ;
	CategorySeries mSeries ; 
	GraphicalView mChartView;
	ItypeDAO itypeDAO;
	TextView nodata;
	private static int[] COLORS = new int[] { Color.rgb(180, 0, 0),Color.rgb(180, 120,130),
		Color.rgb(10, 180, 170),Color.rgb(10, 180, 10),Color.rgb(220, 180, 10),Color.rgb(220, 180, 130),Color.rgb(20, 180, 130)
		,Color.rgb(20, 18, 130),Color.rgb(255, 120, 10),Color.rgb(255, 120, 100),Color.rgb(255, 12, 100),Color.rgb(217, 190, 100)
		,Color.rgb(50,150, 100),Color.rgb(150,150, 100),Color.rgb(150,150, 190)};
	
	public IncomeData() {
		// TODO Auto-generated constructor stub
	}
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.incomedata);

	        SysApplication.getInstance().addActivity(this); // �����ٶ��������this
	        time = new Time("GMT+8");    
	        time.setToNow();   
	        defaultMonth=time.month+1;//����Ĭ���·�
	        defaultYear=time.year;
	        beforet=(Button)findViewById(R.id.ibefore);
	        aftert=(Button)findViewById(R.id.iafter);
	        anytime=(Button)findViewById(R.id.ianytime);
	        year=(Spinner)findViewById(R.id.iyear);
	        month=(Spinner)findViewById(R.id.imonth);
	        day=(Spinner)findViewById(R.id.iday);
	        yeare=(Spinner)findViewById(R.id.iyeare);
	        monthe=(Spinner)findViewById(R.id.imonthe);
	        daye=(Spinner)findViewById(R.id.idaye);
 		    nodata=(TextView)findViewById(R.id.nodata);
 		    idataselect=(LinearLayout)findViewById(R.id.idataselect);
	        piechart=(LinearLayout)findViewById(R.id.ichart);
	        mSeries = new CategorySeries(""); 
	        mRenderer = new DefaultRenderer();// PieChart����Ҫ�����
	        yearlist=new ArrayList<String>(); //��������б� spinner
	        itypeDAO=new ItypeDAO(IncomeData.this);
	        
	        //������
	        for(int i=0;i<=10;i++){
	        	yearlist.add(String.valueOf(defaultYear-i));
	        } 
	        adapter=new ArrayAdapter<String>(IncomeData.this,android.R.layout.simple_spinner_item,yearlist);
	        year.setAdapter((SpinnerAdapter) adapter);
	        yeare.setAdapter((SpinnerAdapter) adapter);
	
	 } 
	 
	 @Override
	 protected void onStart(){
		 	super.onStart();
		 	intentr=getIntent();
		 	userid=intentr.getIntExtra("cwp.id",100000001); 
		 	defaultMonth=intentr.getIntExtra("default", defaultMonth);  
		 	defaultYear=intentr.getIntExtra("defaulty", defaultYear);  
		 	int type=intentr.getIntExtra("type",0);//Ϊ0��ѡ�������£�Ϊ1��ѡ������ʱ��
		 	
		 	//��ͼ
		 	/*      mRenderer=new DefaultRenderer();
			        mRenderer.setApplyBackgroundColor(true);
			        mRenderer.setBackgroundColor(Color.TRANSPARENT);
			//      mRenderer.setChartTitleTextSize(20);
			        mRenderer.setLabelsTextSize(20);
			        mRenderer.setLabelsColor(Color.BLACK);
			        mRenderer.setShowAxes(false);   // �Ƿ���ʾ����
			//      mRenderer.setAxesColor(Color.RED); //��������ɫ
			        mRenderer.setFitLegend(false);
			        mRenderer.setInScroll(false);
			        mRenderer.setPanEnabled(false);
			        mRenderer.setShowCustomTextGrid(false);
			        mRenderer.setShowLegend(false);       //����ʾͼ��
			        mRenderer.setShowGrid(false);
			        mRenderer.setClickEnabled(true);
			//      mRenderer.setScale(1.5f);
			//      mRenderer.setLegendTextSize(15);
			//      mRenderer.setMargins(new int[] { 20, 50, 15, 0 });
			//      mRenderer.setZoomButtonsVisible(true);
			        mRenderer.setStartAngle(45);

		 	*/

		 	mRenderer.setZoomButtonsVisible(true);// ��ʾ�Ŵ���С���ܰ�ť
		 	mRenderer.setStartAngle(180);// ����Ϊˮƽ��ʼ
		 	mRenderer.setDisplayValues(true);// ��ʾ����
		 	mRenderer.setFitLegend(true);// �����Ƿ���ʾͼ��  
		 	mRenderer.setShowLabels(true);  
	        mRenderer.setLabelsTextSize(40);
	        mRenderer.setShowLegend(true);  
	        mRenderer.setLabelsColor(Color.BLACK);
		 	mRenderer.setLegendTextSize(30);// ����ͼ�������С
		 	mRenderer.setLegendHeight(30);// ����ͼ���߶� 
		 	mRenderer.setChartTitleTextSize(34);// ���ñ�ͼ�����С
		 	incomeDAO=new IncomeDAO(IncomeData.this);
		 	
		 	if(type==0){ 
		 		KindDatai=incomeDAO.getKDataOnMonth(userid, defaultYear, defaultMonth); 	
		 		mRenderer.setChartTitle(String.valueOf(defaultYear)+"-"+String.valueOf(defaultMonth));
	 		}else{ 
				 date1=intentr.getStringExtra("date1");
			     date2=intentr.getStringExtra("date2");			     
			     KindDatai=incomeDAO.getKDataOnDay(userid, date1, date2);
			     mRenderer.setChartTitle(date1+"~"+date2); 
		    	 
		     }
		 	//����

		 	 if(KindDatai.size()==0){ 
		 		 nodata.setVisibility(View.VISIBLE); 
		 		 
		 	 }else{
		 		 
			 	double sum=0.00;  
			 	int i=0;
		 		for(KindData ki:KindDatai)
		 			sum+=ki.getAmount();//�ܺ�
		 		for(KindData ki:KindDatai){ 
		 			mSeries.add(itypeDAO.getOneName(userid, ki.getKindname()), ki.getAmount()/ sum);
		 			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
		 			if(i<COLORS.length){
		 				renderer.setColor( COLORS[i]);
		 			}else{
		 				renderer.setColor( getRandomColor());
		 			}
		 			renderer.setChartValuesFormat(NumberFormat.getPercentInstance());// ���ðٷֱ� 
			 		mRenderer.addSeriesRenderer(renderer);// �����µ��������ӵ�DefaultRenderer��
			 		i++;
		 		}
		 		  
		            mChartView = ChartFactory.getPieChartView(getApplicationContext(),  
		                    mSeries, mRenderer);// ����mChartView  
		            mRenderer.setClickEnabled(true);// �������¼�  
		            mChartView.setOnClickListener(new OnClickListener() {// ��������
		                        @Override  
		                        public void onClick(View v) {  
		                            SeriesSelection seriesSelection = mChartView  
		                                    .getCurrentSeriesAndPoint();// ��ȡ��ǰ������ָ��  
		                            if (seriesSelection == null) {  
		                                Toast.makeText(getApplicationContext(),  
		                                        "��δѡ������", Toast.LENGTH_SHORT).show();  
		                            } else {  
		                                for (int i = 0; i < mSeries.getItemCount(); i++) {  
		                                    mRenderer.getSeriesRendererAt(i)  
		                                            .setHighlighted(  
		                                                    i == seriesSelection  
		                                                            .getPointIndex());  
		                                }  
		                                mChartView.repaint();  
		                                Toast.makeText(  
		                                        getApplicationContext(),  
		                                        "��ѡ����ǵ�"  
		                                                + (seriesSelection
		                                                        .getPointIndex() + 1)  
		                                                + " �� "  
		                                                + " �ٷֱ�Ϊ  "  
		                                                + NumberFormat  
		                                                        .getPercentInstance()  
		                                                        .format(seriesSelection  
		                                                                .getValue()),  
		                                        Toast.LENGTH_SHORT).show();  
		                            }  
		                        }  
		                    });  
		            piechart.addView(mChartView);   
		}
		 	beforet.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(defaultMonth!=1)
					defaultMonth=defaultMonth-1;
					else{
						defaultMonth=12;
						defaultYear=defaultYear-1;
					}
					mSeries.clear(); 
					Intent intentp=new Intent(IncomeData.this,IncomeData.class);
					intentp.putExtra("defaulty", defaultYear);
					intentp.putExtra("default",defaultMonth);
					intentp.putExtra("cwp.id", userid); 
					startActivity(intentp);
				}
			});
	 		aftert.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(defaultMonth!=12)
					defaultMonth=defaultMonth+1;
					else{
						defaultMonth=1;
						defaultYear=defaultYear+1;
					}
					mSeries.clear(); 
					Intent intentp=new Intent(IncomeData.this,PayData.class);
					intentp.putExtra("defaulty", defaultYear);
					intentp.putExtra("default",defaultMonth);
					intentp.putExtra("cwp.id", userid); 
					startActivity(intentp);
				}
			});

	 		anytime.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					getAnyDate();
					mSeries.clear();  
				 	Intent intentp=new Intent(IncomeData.this,IncomeData.class);
				 	intentp.putExtra("type",1);
				 	intentp.putExtra("date1", date1);
				 	intentp.putExtra("date2", date2);
				 	intentp.putExtra("cwp.id", userid);
				    startActivity(intentp);
				}
			});
	 	  
		 	 
	 }
	 public void getAnyDate(){
	 		date1=year.getSelectedItem().toString()+"-"+month.getSelectedItem().toString()+"-"+day.getSelectedItem().toString();
	 		date2=yeare.getSelectedItem().toString()+"-"+monthe.getSelectedItem().toString()+"-"+daye.getSelectedItem().toString();

	 	}
	 private int getRandomColor() {// �ֱ����RBG��ֵ  
	        Random random = new Random();  
	        int R = random.nextInt(255);  
	        int G = random.nextInt(255);  
	        int B = random.nextInt(255);  
	        return Color.rgb(R, G, B);  
	    } 

	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK) { //���/����/���η��ؼ�
	    	Intent intent=new Intent(IncomeData.this,MainActivity.class);
			intent.putExtra("cwp.id",userid);
			intent.putExtra("cwp.Fragment", "2");// ���ô�������  
			startActivity(intent);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
		
}
