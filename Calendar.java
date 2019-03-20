package project4;

import java.time.*;


import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;



public class Calendar extends Application
{
   private BorderPane containerPane = new BorderPane();
   private LocalDateTime date = LocalDateTime.now();
   private String appointmentFile = "src/project4/appointmentFile.csv";
   private Scene scene;

   private HBox topBox = new HBox();
   private String[] firstLetters = { "S", "M", "T", "W", "T", "F", "S" };
   //Use this stage if you decide to complete the extra credit
   /*
   private Stage appointmentStage = new Stage();
   */

   @Override
   public void start(Stage primaryStage)
   {
      scene = new Scene(containerPane, 1000, 800);
      containerPane.setStyle("-fx-background-color: whitesmoke;");
      setupTopPane();
      GridPane gp = setupMonthPane(date.getYear(), date.getMonthValue());
      containerPane.setCenter(gp);
        
      primaryStage.setTitle("Calendar");
      primaryStage.setMinWidth(1000);
      primaryStage.setMinHeight(800);
      primaryStage.setScene(scene);
      primaryStage.show();
        
        
      //Use the following if you decide to complete the extra credit
      /*
      appointmentScene = new Scene(appointmentPane, 350, 250);
      setupAppointmentPane();
      appointmentStage.setTitle("Add Event");
      appointmentStage.setScene(appointmentScene);
      */
   }
    
   public void setupTopPane()
   {
      topBox.setPadding(new Insets(15));
      topBox.setSpacing(10);

      Text currentMonth = new Text(date.getMonth() + "");
      currentMonth.setFont(new Font(20));
      Text currentYear = new Text(date.getYear() + "");
      currentYear.setFont(new Font(20));
      Region topSpacer = new Region();

      HBox nav = new HBox();
      Button backBtn = new Button("<");
      Button forwardBtn = new Button(">");
      Button yearBtn = new Button("Year");
      yearBtn.setMinWidth(70);
      Button todayBtn = new Button("Today");
      todayBtn.setMinWidth(70);
      nav.getChildren().addAll(backBtn,yearBtn,todayBtn,forwardBtn);
      nav.setSpacing(5);

      topBox.getChildren().addAll(currentMonth,currentYear,topSpacer,nav);
      topBox.setHgrow(topSpacer, Priority.ALWAYS);
   
      containerPane.setTop(topBox);
   }
   
   public GridPane setupMonthPane(int yearValue, int monthValue)
   {
      GridPane monthPane = new GridPane();



      // Setting up the initial grid with visible lines
      monthPane.setGridLinesVisible(true);
      monthPane.setAlignment(Pos.TOP_CENTER);
      for(int i = 0; i < 7; i++)
      {
         ColumnConstraints col = new ColumnConstraints();
         col.setPercentWidth(100/7);
         monthPane.getColumnConstraints().add(col);

         if(i == 0)
            monthPane.getRowConstraints().add(new RowConstraints(40));
         else
         {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100/7);
            monthPane.getRowConstraints().add(row);
         }

      }

      // Setting up the top row of the grid for days of the week
      for(int col = 0; col < 7; col++)
      {
         for(int row = 0; row < 7; row++)
         {
            if(row == 0)
            {
               Text text = new Text(firstLetters[col]);
               text.setTextAlignment(TextAlignment.CENTER);
               monthPane.add(text, col, row);
               monthPane.setHalignment(text, HPos.CENTER);
            }
         }
      }

      // At this point I need to call the next method to fill up the gridpane
        fillUpMonth(monthPane, yearValue, monthValue);
        
      return monthPane;
   }
   
   public void fillUpMonth(GridPane monthGP, int yearValue, int monthValue)
   {
      YearMonth yearMonth = YearMonth.of(yearValue,monthValue);
      DayOfWeek firstDayOfWeek = yearMonth.atDay(1).getDayOfWeek();

      YearMonth preMonth = yearMonth.minusMonths(1);

      int preMonthDays = 0;
      if (!firstDayOfWeek.equals(DayOfWeek.SUNDAY))
      {
         preMonthDays = preMonth.lengthOfMonth() - firstDayOfWeek.getValue();
      }

      int days = yearMonth.lengthOfMonth();
      int count = 1;
      int nextMonthDays = 1;

      for(int row = 1; row < 7; row++)
      {
         for(int col = 0; col < 7; col++)
         {
            if(preMonthDays != preMonth.lengthOfMonth())
            {
               preMonthDays++;
               Text text = new Text(preMonthDays + "");
               text.setTextAlignment(TextAlignment.CENTER);
               text.setFill(Color.GREY);
               monthGP.add(text, col, row);
               monthGP.setHalignment(text, HPos.CENTER);

            }
            else if(count <= days ) {

               if (count == date.getDayOfMonth())
               {
                  StackPane sp = new StackPane();
                  Circle circle = new Circle(20, Color.RED);
                  Text currentDate = new Text(count + "");
                  currentDate.setFill(Color.WHITE);
                  sp.getChildren().addAll(circle, currentDate);
                  monthGP.add(sp, col, row);
                  count++;
               }
               else
               {
                  Text text = new Text(count + "");
                  text.setTextAlignment(TextAlignment.CENTER);
                  monthGP.add(text, col, row);
                  monthGP.setHalignment(text, HPos.CENTER);
                  count++;
               }

            }
            else
            {
               Text text = new Text(nextMonthDays + "");
               text.setTextAlignment(TextAlignment.CENTER);
               text.setFill(Color.GREY);
               monthGP.add(text, col, row);
               monthGP.setHalignment(text, HPos.CENTER);
               nextMonthDays++;
            }
         }
      }
   }
    
   public GridPane twelveMonthsPane()
   {
      GridPane twelve = new GridPane();
      //TO BE COMPLETED AS REQUIRED IN THE INSTRUCTIONS
        
        
        
        
      return twelve;
   }
   
   
   
   //The following is for the extra credit
    
   public void setupAppointmentPane()
   {
        //TO BE COMPLETED AS REQUIRED IN THE INSTRUCTIONS
        
        
        
        
   }
    
   public void displayAppointments(GridPane monthPane)
   {
      //TO BE COMPLETED AS REQUIRED IN THE INSTRUCTIONS



   }

   public void clear()
   {
      //TO BE COMPLETED AS REQUIRED IN THE INSTRUCTIONS
      
      
      
   }

    
   public void storeAppointment(StackPane sp)
   {   
      //TO BE COMPLETED AS REQUIRED IN THE INSTRUCTIONS
    
   }

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args)
   {
       launch(args);
   }

}
