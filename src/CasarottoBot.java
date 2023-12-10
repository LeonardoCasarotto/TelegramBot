import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import javax.validation.constraints.Null;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class CasarottoBot extends TelegramLongPollingBot {

    private boolean quizInProgress=false;
    private int mistakes;
    int currentQuestion;
    int totalQuestions;
    int messageId;

    final String username = "PatenteBot";
    private boolean isAnswerng = false;
    private Long chatId;
    public JSONArray questionsStored;
    String url = "https://tuttopatenti.it/risposte-corrette-argomenti-quiz-patente-a-b.php";
    String urlQuest = "https://tuttopatenti.it/quiz-patente-b/risposte-corrette/";
    private Update update;


    //list quiz arguments
    private ArrayList<String> addBtns() throws IOException {


        Document subjList = Jsoup.connect(url).get();
        Elements subjects = subjList.select(".blockdx >p");


        ArrayList<String> stringList = new ArrayList<>();

        for (Element subject : subjects) {

            stringList.add(subject.text());

        }
        return stringList;
    }



    @Override
    public String getBotUsername() {
        return username;

    }

    @Override
    public String getBotToken() {
        return "6695172016:AAHW-vc872Px3wTawDkPJQ-0TLBW_IIty-8";

    }


    //method for handling messages
    @Override
    public void onUpdateReceived(Update update) {


        if (update.hasMessage() && update.getMessage().hasText()) {
            String receivedText = update.getMessage().getText();
            this.chatId = update.getMessage().getChatId();


            if (receivedText.equals("/pratica")) {
                try {
                    ArrayList<String> btns = addBtns();

                    ArrayList<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
                    List<InlineKeyboardButton> row = new ArrayList<>();

                    for (String buttonText : btns) {
                        InlineKeyboardButton temp = new InlineKeyboardButton();
                        temp.setText(buttonText);

                        temp.setCallbackData(buttonText);

                        row.add(temp);

                        // You can change the number of buttons per row as needed
                        if (row.size() >= 2) {
                            keyboard.add(row);
                            row = new ArrayList<>();
                        }
                    }

                    if (row.size() > 0) {
                        keyboard.add(row);
                    }

                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

                    markupInline.setKeyboard(keyboard);

                    SendMessage toSend = new SendMessage();
                    toSend.setChatId(this.chatId.toString());
                    toSend.setText("Scegliere un'opzione:");
                    toSend.setReplyMarkup(markupInline);

                    Send(toSend);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else if (receivedText.equals("/start")){
                SendMessage toSend = new SendMessage();
                toSend.setText("Benvenuto! Sono il Bot della Patente di Guida 🪪.\n\nPosso aiutarti a praticare per l'esame 📝 della patente. \n\nUtilizza il comando /pratica per avviare un quiz su specifici argomenti dell'esame. Puoi scegliere il numero di domande da affrontare selezionando il numero desiderato. Buona fortuna! 🎉");
                Send(toSend);
            }
            else if (receivedText.equals("/help")) {

                SendMessage toSend = new SendMessage();
                toSend.setText("toSend.setText(\"Ciao! Sono il Bot della Patente di Guida.\\n\\nEcco alcuni comandi che puoi utilizzare:\\n\\n\"\n" +
                        "    + \"/start - Avvia il bot e ricevi un messaggio di benvenuto.\\n\"\n" +
                        "    + \"/pratica - Avvia un quiz sulla patente di guida su argomenti specifici. Puoi scegliere il numero di domande.\\n\"\n" +
                        "    + \"/help - Visualizza questo messaggio di aiuto.\\n\\n\"\n" +
                        "    + \"/wip - Visualizza una lista delle funzioni in via di sviluppo.\\n\\n\"\n" +
                        "    + \"Se hai bisogno di ulteriore assistenza, non esitare a contattarmi!\");\n");
                Send(toSend);
            }

            else if( receivedText.equals("/wip")){
                SendMessage toSend = new SendMessage();
                toSend.setText(
                        " Ecco le funzioni in via di sviluppo, le troverai presto sul bot!:\n\n" +
                                "1. Fare quiz su tutti gli argomenti 🔀 (nuovo argomento nell'apposita sezione).\n" +
                                "2. Visualizzare la teoria in base alla domanda 📕 (/teoria dopo una domanda che non si conosce).\n\n" +
                                "Potrai utilizzare i comandi per accedere a queste funzioni e migliorare la tua preparazione per l'esame. Continua a utilizzare il bot per essere il primo ad utilizzarle! ✅"
                );
                Send(toSend);

            }

        }


        //if a button is pressed
        else if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            Long messageChatId = callbackQuery.getMessage().getChatId();

            if (callbackData.equals("5") || callbackData.equals("10") || callbackData.equals("20") || callbackData.equals("30")) {


                totalQuestions = Integer.parseInt(callbackData);
                mistakes=0;
                currentQuestion=0;
                quizInProgress = true;
                Start(totalQuestions);

            }



            else if(messageChatId.equals(chatId)) {
                SendMessage result = new SendMessage();



                if(callbackData.equals("true")){

                    result.setText("Risposta corretta!");





                    Send(result);






                }
                else if (callbackData.equals("false")){
                    mistakes++;
                    result.setText("Risposta errata!");
                    Send(result);





                }
                if(currentQuestion<totalQuestions&&quizInProgress){
                    presentNextQuestion(currentQuestion, totalQuestions);}

                else{
                    SendMessage toSend = new SendMessage();
                    toSend.setText("Hai terminato il quiz, facendo "+ mistakes+" errori");
                    Send(toSend);
                }





            }


            SendMessage toSend = new SendMessage();
            toSend.setChatId(callbackQuery.getMessage().getChatId().toString());

            String toConn = urlQuest + callbackData.toLowerCase().replace(" ", "-") + ".php";

            try {
                Document questionsList = Jsoup.connect(toConn).get();
                Elements questionsDiv = questionsList.select(".lista");
                Elements questions = questionsList.select(".lista> .domanda");
                String[] imagesLinks = new String[questions.size()];
                String[] solutions = new String[questions.size()];


                for (int i = 0; i < questions.size(); i++) {
                    imagesLinks[i] = "https://www.tuttopatenti.it" + questionsDiv.get(i).select("img").attr("src");
                    solutions[i] = questionsDiv.get(i).select(".risposta").attr("id");
                }

                JSONArray data = new JSONArray();


                for (int x = 0; x < questions.size(); x++) {

                    JSONObject temp = new JSONObject();

                    temp.put("image", imagesLinks[x]);
                    temp.put("question", questions.get(x).text());
                    temp.put("solution", solutions[x]);

                    data.put(temp);
                }
                questionsStored = data;


                //toSend.setText(data.getJSONObject(0).toString());
                toSend.setText("Inserire il numero delle domande che si desidera affrontare sull'argomento");



                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

                List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
                List<InlineKeyboardButton> firstrow = new ArrayList<>();
                List<InlineKeyboardButton> secndrow = new ArrayList<>();

                InlineKeyboardButton fiveButton = new InlineKeyboardButton();

                fiveButton.setText("5");
                fiveButton.setCallbackData("5");

                firstrow.add(fiveButton);

                InlineKeyboardButton tenButton = new InlineKeyboardButton();
                tenButton.setText("10");
                tenButton.setCallbackData("10");

                firstrow.add(tenButton);

                InlineKeyboardButton twentyButton = new InlineKeyboardButton();
                twentyButton.setText("20");
                twentyButton.setCallbackData("20");

                secndrow.add(twentyButton);


                InlineKeyboardButton thirtyButton = new InlineKeyboardButton();
                thirtyButton.setText("30");
                thirtyButton.setCallbackData("30");

                secndrow.add(thirtyButton);

                keyboard.add(firstrow);
                keyboard.add(secndrow);
                markupInline.setKeyboard(keyboard);
                toSend.setReplyMarkup(markupInline);














            } catch (IOException e) {
                throw new RuntimeException(e);
            }




            Send(toSend);


        }
    }



    //methods to start and continue the quiz



    private void Start(int totalQuestions){

        presentNextQuestion(0,totalQuestions);



    }
    private void presentNextQuestion(int currentQuestionIndex, int totalQuestions) {


        if (quizInProgress&&currentQuestionIndex < totalQuestions) {

            int questionindex = new Random().nextInt(questionsStored.toList().size());

            if (hasImage(questionindex)) {

                SendPhoto sendPhoto = new SendPhoto();


                try {
                    //i am getting error here when try to get the image
                    URL imgUrl = new URL(questionsStored.getJSONObject(questionindex).getString("image"));
                    InputStream is = imgUrl.openStream();
                    sendPhoto.setPhoto(new InputFile(is, "domanda.jpg"));
                    sendPhoto.setCaption(questionsStored.getJSONObject(questionindex).getString("question"));

                    boolean answ = false;
                    if (questionsStored.getJSONObject(questionindex).getString("solution").equals("vero")) {
                        answ = true;
                    }

                    InlineKeyboardMarkup veroFalso = createTrueFalseKeyboard(answ);
                    sendPhoto.setReplyMarkup(veroFalso);
                    sendPhoto.setChatId(chatId);

                    execute(sendPhoto);
                    //return;



                } catch (IOException | TelegramApiException e) {
                    throw new RuntimeException(e);
                }


            }
            else {
                SendMessage toSend = new SendMessage();
                toSend.setText(questionsStored.getJSONObject(questionindex).getString("question"));

                boolean answ = false;
                if (questionsStored.getJSONObject(questionindex).getString("solution").equals("vero")) {
                    answ = true;
                }
                InlineKeyboardMarkup veroFalso = createTrueFalseKeyboard(answ);
                toSend.setReplyMarkup(veroFalso);

                Send(toSend);
            }
            currentQuestion++;





        }
        else {
            quizInProgress = false;
            totalQuestions = 0;
        }






    }


    //utils method
    private boolean hasImage(int index) throws NullPointerException {

        if (questionsStored == null) {
            throw new NullPointerException();
        } else if (questionsStored.getJSONObject(index).getString("image").equals("https://www.tuttopatenti.it")) {

            return false;

        } else return true;
    }

    private InlineKeyboardMarkup createTrueFalseKeyboard(boolean correct) {


        //the "correct" boolean value works in this way:
        //if it is false (0) the correct answer is FALSE
        //if is true (1) the correct answer is TRUE


        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton trueButton = new InlineKeyboardButton();
        trueButton.setText("Vero");
        if (correct) {
            trueButton.setCallbackData("true");
        } else {
            trueButton.setCallbackData("false");
        }

        InlineKeyboardButton falseButton = new InlineKeyboardButton();
        falseButton.setText("Falso");

        if (correct) {
            falseButton.setCallbackData("false");
        } else {
            falseButton.setCallbackData("true");
        }


        row.add(trueButton);
        row.add(falseButton);
        keyboard.add(row);

        markupInline.setKeyboard(keyboard);
        return markupInline;
    }


    public void Send(SendMessage ms){
        try{
            ms.setChatId(chatId);
            execute(ms);


        }
        catch (TelegramApiException e){
            throw  new RuntimeException();
        }

    }



}