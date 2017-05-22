
package queue_simulator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;



/**
 *
 * @author Wycliffe
 */
public class Queue_executor {

    
   //For reading 
  static FileInputStream infile = null;
  static FileWriter outfile = null;
  static BufferedReader msomaji = null;
  static BufferedWriter mwandishi = null;
  
   private static final int Q_LIMIT = 100;
   // The server status IDLE state  oscillate 0 or 1.
   
    
    //Other varibles
   private static int next_event_type, num_custs_delayed, 
           num_events, num_in_q;
   
   private static int IDLE = 0, server_status, num_delays_required;
    
   private static double area_num_in_q, area_server_status, mean_interarrival,
        mean_service, time,time_last_event, total_of_delays;
   
   private static double[] time_next_event = new double[3];
   private static double[] time_arrival = new double[Q_LIMIT +1] ; 
   

    
            
  
    
    /* The initialize method*/
    
public static void initialize(){
    /* Initialize the simulation clock. */
time = (double) 0.0;
/* Initialize the state variables. */
server_status = IDLE;
num_in_q = 0;
time_last_event = (double) 0.0;

/* Initialize the statistical counters. */
num_custs_delayed= 0;
total_of_delays = (double) 0.0;
area_num_in_q = (double) 0.0;
area_server_status = (double) 0.0;

/* Initialize event list. since no customers are present, the
departure (service completion) event is eliminated from
consideration. */
time_next_event[1] = time + expon(mean_interarrival);
time_next_event[2] = 1.0+30; 

}// end initialize()





/*  ---------------------- The Timing Method-----------------------------*/
//compare time_next_event[1],time_next_event[2]
public static void timing () {
    
    int i;
    
    double min_time_next_event = 1.0e+30;
    next_event_type = 0;
    
    /* Determine the event type of the next event to occur. */
    for(i= 1; i<= num_events; ++i){
       if(time_next_event[i] < min_time_next_event){
       min_time_next_event = time_next_event[i];
       next_event_type = i;
       }
     }
    
    
    
    /* Check to see whether the event list is empty. */
    if( next_event_type == 0){
    /* The event list is empty, so stop the simulation. */
        
       System.out.println(/* Supposed to print out the output,*/ " Event list is empty at time:  " + time);
       
    }
    /* The event list is not empty, so advance the simulation clock. */
    time = min_time_next_event;   
}//end timing()





/*=================== Arrival event function.==================================== */
public static void arrive (){
    double delay;
    
    /* Schedule next arrival. */
    time_next_event[1] = time + expon(mean_interarrival); // be sure to implement expon
    
    /* Check to see whether server is busy. */
    if(server_status == 1){
    /* Server is busy, so increment number of customers in queue.*/
        
        ++num_in_q;
      /* Check to see whether an overflow condition exists. */
        if(num_in_q >  Q_LIMIT){
            
            /* The queue has overflowed, so stop the simulation. */ /*HERE HERE*/
            System.out.println("\nOverflow of the array time_arrival at");
            System.out.println(" time " + time);
            
            // This exit function ???
         }
        /* There is still room in the queue, so store the time of
arrival of the arriving customer at the (new) _end of
time_arrival. */
        time_arrival[num_in_q] = time;
       }
    
    else{
    
         /* Server is idle, so arriving customer has a delay of zero.
(The following two statements are for program clarity and do
not affect the results of the simulation.) */
      delay = (double) 0.0;
      total_of_delays += delay;
   
      /* Increment the number of customers delayed, and make server
busy. _*/
      ++num_custs_delayed;
      server_status = 1; // for busy
     
      /****** Schedule a departure (service completion). ******/
      
      time_next_event[2] = time + expon(mean_service);
      
      
    }
}//end arrive()   
 




/* -------------- Departure event function ------------------*/
public static void depart(){
   
    int i;
    double delay;
    
    /* Check to see whether the queue is empty. */
    if(num_in_q == 0){
    
        /* The queue is empty so make the server idle and eliminate the
departure (service completion) event from consideration. */
        server_status = IDLE;
        time_next_event[2] = 1.0e+30;    
    }
    else{
    /* queue nonempty, so decrement the nUmber of customers in queue.*/
        --num_in_q;
      
     /* Compute the delay of the customer who is beginning service
and update the total delay accumulator. */
        delay = time - time_arrival[1];
        total_of_delays += delay;
        
      /* Increment the number of customers delayed, and schedule
departure. */  
        ++num_custs_delayed;
        time_next_event[2] = time + expon(mean_service);
        
        /* Move each customer in queue (if any) up one place. */
        for(i= 1 ; i<= num_in_q ; ++i){
        time_arrival[i] = time_arrival[i+1];
        }
        
    }
} //end depart()




public static void report() throws IOException{
    //4.final clock time, 3.Server utilization, 2.average delay, 1.time average no in queue
    /* compute and write estimates of desired measures of performance.
*/
    
//    System.out.println("Average delay in queue "+ total_of_delays / num_custs_delayed + "minutes\n\n");
//    System.out.println("Average number in queue " + area_num_in_q / time + "\n\n");
//    System.out.println("Server utilization " + area_server_status / time +"\n\n");
//    System.out.println("Time simulation ended " + time);
    
    //WRITING INTO THE OUTPUT FILE  
    try{
    //Writing to a file 
       System.out.println("Writing done");
       
       mwandishi.write("\t\t SINGLE SERVER QUEUING SYSTEM ");
       mwandishi.newLine();
       mwandishi.newLine(); 
    
       mwandishi.append("Mean interarrival time\t\t"+  mean_interarrival+ "  minutes") ;
       mwandishi.newLine();
       
       mwandishi.append("Mean service time \t\t" + mean_service+ "  minutes") ;
       mwandishi.newLine();
       
       mwandishi.append("Number of customers\t\t "+ num_delays_required) ;
       mwandishi.newLine();
       mwandishi.newLine();
       mwandishi.newLine();
       mwandishi.append("Average delay in queue:    "+ total_of_delays / num_custs_delayed + "  minutes") ;
       mwandishi.newLine();
       
       mwandishi.append("Average number in queue:   " + area_num_in_q / time ) ;
       mwandishi.newLine();
       
       mwandishi.append("Server utilization:        " + area_server_status / time ) ;
       mwandishi.newLine();
       
       mwandishi.append("Time simulation ended:     " + time+ "  minutes") ;
       mwandishi.newLine();
      
       mwandishi.close();
       
    }
    catch(IOException i){
    System.out.println( " Error writing in the report function :  " + i.getMessage());
    }
    
}// end report()



/* Update area accumulators for
time-average statistics. */
public static void update_time_avg_stats(){
  double time_since_last_event;
  
  /* compute time"since last event, and update last-event-time
marker. */
  time_since_last_event = time- time_last_event;
  time_last_event = time;
  
  /*Update area under number-in-queue function. */
  area_num_in_q += num_in_q * time_since_last_event;
  
  /*'Update area under server-busy indicator function. */
  area_server_status += (server_status * time_since_last_event);
    
} //end update_time_avg_stats()




// ======================= THE EXPON METHOD ================= VERRY IMPORTANT.
public static double expon (double mean){ 
    
    double u;  
    /* Generate a U(0,1) random variate. */
    u= (double) Math.random();
    
    /* Return an exponential random variate with mean "mean". */  
    return (double) (-mean * Math.log(u));
}
    
    



 //======================== main method ==========================
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
       
       /* specify the number of events for the timing function. */
       num_events = 2;
        
       try{
  // ==================== File operation Declarations ==========================
       //opening/accessing the input and output files
        infile = new FileInputStream("C:\\Users\\Wycliffe\\Documents\\NetBeansProjects\\SIMULATION AND MODELING\\infile.txt");
        outfile = new FileWriter ("C:\\Users\\Wycliffe\\Documents\\NetBeansProjects\\SIMULATION AND MODELING\\outfile.txt");
        
        msomaji = new BufferedReader(new InputStreamReader(infile));
        mwandishi = new BufferedWriter(outfile);
        
        
        
       }catch (IOException e){
        System.out.println("the Exception message:  " + e.getMessage());
       } 
       
       
       // Reading input parameters
        String line = msomaji.readLine();
        String[] values = line.split(",");
        
        
        mean_interarrival = Double.parseDouble(values[0]);
        mean_service = Double.parseDouble(values[1]);
        num_delays_required = Integer.parseInt(values[2]);
        
        
       //------------------------------------------------------- the meat ------------------------
         initialize ();
         
         /* Run the simulation while more delays are 'still needed. */
         while(num_custs_delayed < num_delays_required){
         /* Determine the next event. */
             timing();
             
         /* Update time-average statistical accumulators. */
             update_time_avg_stats();
           
         /* Invoke the' appropriate event function. */
             switch(next_event_type){
                 case 1:
                     arrive();
                     break;
                 case 2:
                     depart();
                     break;
             }
         
         }
         
         /* Invoke the report generator and end the s~mulation. */
         report();
         
      }// End main
    
}
