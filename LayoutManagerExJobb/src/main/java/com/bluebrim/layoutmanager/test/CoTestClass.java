package com.bluebrim.layoutmanager.test;

import java.util.*;

import javax.swing.*;

import com.bluebrim.layoutmanager.*;
/**
 * Insert the type's description here.
 * Creation date: (2000-06-06 12:33:25)
 * @author: Arvid Berg & Masod Jalalian 
 */
public class CoTestClass {
/**
 * CoTestClass constructor comment.
 */
public CoTestClass() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-06 12:34:35)
 */
public static void layout(List list) 
{
	/*- Create column list */
	List columnlist=new ArrayList();
	for (int i = 0; i < 5; i++)
	{	
		columnlist.add(new CoColumnElementDefault(i*100,0));
	}
	double columnWidth=((CoColumnElement)columnlist.get(0)).getWidth();
	double columnHeight=((CoColumnElement)columnlist.get(0)).getHeight();
	List lock=new ArrayList();
	/*- Create free intervalls */
	Iterator iter=lock.iterator();
	while(iter.hasNext())
	{
		RectIMLayoutable rect=(RectIMLayoutable)iter.next();
		
		for (int i = (int)(rect.getX()/100); 
			i <= (int)((rect.getX()+rect.getLayoutWidth())/100); i++)
		{
			((CoColumnElement)columnlist.get(i)).add(new CoInterval(rect.getY(),
				rect.getY()+rect.getLayoutHeight()));
		}	
	}
	iter=columnlist.iterator();
	while(iter.hasNext())
	{
		CoColumnElement col=(CoColumnElement)iter.next();
		col.setIntervalls( CoInterval.invert(col.getIntervalls(),
			new CoInterval(col.getY(),col.getY()+col.getHeight()))  );					
	}
	/*- Get children to layout */
	int size_in_columns=-1,tmp;
	List[] array=new List[columnlist.size()];
	List freeList=new LinkedList();
	iter=list.iterator();
	/*- Repeat to all children is placed*/
	while(iter.hasNext())	
	{
		RectIMLayoutable rect=(RectIMLayoutable)iter.next();
		/*- If first time or size_in_columns is not same as last*/
		if(size_in_columns!=rect.getLayoutWidth()/ columnWidth)
		{
			/*- Create lists for (columnCount-size_in_columns+1)*/
			size_in_columns=(int)(rect.getLayoutWidth()/ columnWidth);
			for (int i = 0; i < (columnlist.size()-size_in_columns+1); i++)
			{		
				freeList.clear();				
				
				for(int j=0;j<size_in_columns;j++)
				{
					freeList.addAll( ((CoColumnElement)columnlist.
						get(j+i)).getIntervalls());
				}
				array[i]=CoInterval.merge(freeList);
			}			
		}
		/*- For each intervall in lists compare and store best choice*/
		CoSolution choice=new CoSolution(),tempSolve;
		for(int i=0;i<(columnlist.size()-size_in_columns+1);i++)
		{
			Iterator iter1=array[i].iterator();
			int columnCount=columnlist.size();
			double cost=Double.MAX_VALUE;
			while(iter1.hasNext())
			{
				CoInterval ival=(CoInterval)iter1.next();
				if(ival.getSize()<rect.getLayoutHeight())
					continue;
				cost=((columnCount-(i+1))*columnWidth)*
					((columnCount-(i+1))*columnWidth)+
					(columnHeight-ival.getStop())*(columnHeight-ival.getStop()) ;
				tempSolve=new CoSolution(i,ival,cost);
				if(tempSolve.compareTo(choice)<0)
					choice=tempSolve;
			}
		}
		/*- Add rect to layouted and update intervalls */
		if(choice.getColumn()!=-1)
		{
			rect.setLayoutSuccess(true);
			rect.setLayoutX(choice.getColumn()*columnWidth);
			rect.setLayoutY(choice.getIntervall().getStop()-rect.getLayoutHeight());
			CoInterval rectinter=null;
			rectinter=new CoInterval(rect.getY(),rect.getY()+rect.getLayoutHeight());
			// Change all the efected columnsets from curent-(size_in_columns-1) to curent+(size_in_columns-1)
			for(int i=(i=choice.getColumn()-size_in_columns+1)<0?0:i;
				i<(choice.getColumn()+size_in_columns) && 
					i<(columnlist.size()-size_in_columns+1);i++)
			{
				//int i=choice.getColumn()-size_in_columns;
				//int i=(i=choice.getColumn()-size_in_columns)<0?0:i;
				List tempList=new LinkedList();
				Iterator it=array[i].iterator();
				while(it.hasNext())
				{
					CoInterval curentIntervall=(CoInterval)it.next();
					Collection tempIntervall=curentIntervall.intersection(choice.getIntervall());
					if(tempIntervall.size()!=0 )
					{
						//array[i].remove(curentIntervall);			
						//array[i].addAll(curentIntervall.difference(rectinter));
						tempList.addAll(curentIntervall.difference(rectinter));
					}
					else
						tempList.add(curentIntervall);
				}
				array[i]=tempList;
			}
			for(int i = 0; i < size_in_columns; i++)
			{	
				List tmpList=((CoColumnElement)columnlist.get(choice.getColumn()+i)).
					getIntervalls();
				Iterator iter2=tmpList.iterator();
				while(iter2.hasNext())
				{
					CoInterval listIntervall=(CoInterval)iter2.next();					
					tmpList.remove(listIntervall);
					tmpList.addAll(listIntervall.difference(rectinter));
					break;				
				}
			}
		}
		
	}
	/*- end repeat*/	
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-06 16:35:45)
 * @param args java.lang.String[]
 */
public static void main(String[] args) 
{
	JFrame frame=new JFrame();
	frame.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(java.awt.event.WindowEvent e)
			{
				System.out.println("Ending program...");
				System.exit(0);
			}
		});
	
	List list=new ArrayList();
	frame.setSize(550,600);
	JPanel jp=new JPanel();
	jp.setSize(50,50);
	CoTestButton butt=new CoTestButton(list,jp);
	butt.addActionListener(butt);
	jp.add(butt);
	frame.getContentPane().add(java.awt.BorderLayout.NORTH,jp);
	CoTestPanel panel=new CoTestPanel(list);
	frame.getContentPane().add(panel);
	frame.show();
	
}
}
