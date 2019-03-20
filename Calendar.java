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
   private YearMonth yearMonthView = YearMonth.from(date);
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

      backBtn.setOnAction(event -> {
         yearMonthView = yearMonthView.minusMonths(1);
         currentMonth.setText(yearMonthView.getMonth() + "");
         currentYear.setText(yearMonthView.getYear() + "");
         GridPane previousPane = setupMonthPane(yearMonthView.getYear(),yearMonthView.getMonthValue());
         containerPane.setCenter(previousPane);
      });

      forwardBtn.setOnAction(event -> {
         yearMonthView = yearMonthView.plusMonths(1);
         currentMonth.setText(yearMonthView.getMonth() + "");
         currentYear.setText(yearMonthView.getYear() + "");
         GridPane nextPane = setupMonthPane(yearMonthView.getYear(),yearMonthView.getMonthValue());
         containerPane.setCenter(nextPane);
      });

      yearBtn.setOnAction(event -> {
         currentMonth.setText("");
         currentYear.setText(yearMonthView.getYear() + "");

      });

      todayBtn.setOnAction(event -> {
         yearMonthView = yearMonthView.from(date);
         currentMonth.setText(date.getMonth() + "");
         currentYear.setText(date.getYear() + "");
         GridPane todayPane = setupMonthPane(yearMonthView.getYear(),yearMonthView.getMonthValue());
         containerPane.setCenter(todayPane);
      });

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
      YearMonth preMonth = yearMonth.minusMonths(1);
      DayOfWeek firstDayOfWeek = yearMonth.atDay(1).getDayOfWeek();

      int currentMonthDay = 1;
      int nextMonthDays = 1;
      int preMonthDays = 0;
      if (!firstDayOfWeek.equals(DayOfWeek.SUNDAY))
      {
         preMonthDays = firstDayOfWeek.getValue();
      }

      for(int row = 1; row < 7; row++)
      {
         for(int col = 0; col < 7; col++)
         {
            if(preMonthDays == 0 && currentMonthDay <= yearMonth.lengthOfMonth())
            {
               StackPane sp = new StackPane();
               Text currentDate = new Text(currentMonthDay + "");
               if(currentMonthDay == date.getDayOfMonth() &&
                       (yearMonth.getYear() == date.getYear()) &&
                       (yearMonthView.getMonthValue() == date.getMonthValue()))
               {
                  currentDate.setFill(Color.WHITE);
                  Circle circle = new Circle(20, Color.RED);
                  sp.getChildren().addAll(circle, currentDate);
               }
               else
               {
                  sp.getChildren().add(currentDate);
               }
               monthGP.add(sp, col, row);
               currentMonthDay++;
            }
            else if(preMonthDays > 0)
            {
               preMonthDays--;
               StackPane sp = new StackPane();
               Text prevDate = new Text(preMonth.lengthOfMonth()- preMonthDays + "");
               prevDate.setFill(Color.GREY);
               sp.getChildren().add(prevDate);
               monthGP.add(sp, col, row);
            }
            else
            {
               StackPane sp = new StackPane();
               Text nextDate = new Text(nextMonthDays + "");
               nextDate.setFill(Color.GREY);
               sp.getChildren().add(nextDate);
               monthGP.add(sp, col, row);
               nextMonthDays++;
            }
         }
      }
   }
    
   public GridPane twelveMonthsPane()
   {
      GridPane twelve = new GridPane();
      //TO BE COMPLETED AS REQUIRED IN THE INSTRUCTIONS

      for(int i = 0; i < 3; i++)
      {
         RowConstraints row = new RowConstraints();
         row.setPercentHeight(100/3);
         twelve.getRowConstraints().add(row);
      }
      for(int col = 0; col < 4; col++)
      {
         ColumnConstraints column = new ColumnConstraints();
         column.setPercentWidth(100/4);
         twelve.getColumnConstraints().add(column);
      }

        
        
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
