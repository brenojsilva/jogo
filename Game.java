import java.util.ArrayList;
import java.util.List;

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private int chavesEncontradas;

    public Game() 
    {
        createRooms();
        parser = new Parser();
        chavesEncontradas = 0;
    }

    private void createRooms()
    {
        Room entrada, sala, quarto, armas, masmorra, banheiro, cozinha, torre, quintal;
      
        // create the rooms
        sala = new Room("sala");
        entrada = new Room("entrada");
        quarto = new Room("quarto");
        armas = new Room("armas");
        masmorra = new Room("masmorra");
        banheiro = new Room("banheiro");
        cozinha = new Room("cozinha");
        torre = new Room("torre");
        quintal = new Room(" quintal");
        
        // initialise room exits
        sala.setExit("sul", entrada);
        sala.setExit("norte", banheiro);
        sala.setExit("oeste", armas);
        sala.setExit("leste", masmorra);

        entrada.setExit("leste", cozinha);
        entrada.setExit("oeste", quarto);
        entrada.setExit("norte", sala);

        masmorra.setExit("sul", cozinha);
        masmorra.setExit("norte", torre);
        masmorra.setExit("oeste", sala);
        
        torre.setExit("oeste", banheiro);
        torre.setExit("sul", masmorra);

        banheiro.setExit("oeste", quintal);
        banheiro.setExit("sul", sala);
        banheiro.setExit("leste", torre);

        quintal.setExit("sul", armas);
        quintal.setExit("leste", banheiro);

        armas.setExit("sul", quarto);
        armas.setExit("leste", sala);
        armas.setExit("norte", quintal);

        quarto.setExit("leste", entrada);
        quarto.setExit("norte", armas);

        cozinha.setExit("norte", masmorra);
        cozinha.setExit("oeste", entrada);

        currentRoom = entrada;  // start game outside

        Item copo = new Item("copo", "você encontrou um copo");
        Item espada = new Item("espada", "você encontrou uma espada");
        Item roupas = new Item("roupas", "você encontrou roupas");
        Item chave = new Item("chave", "você encontrou uma chave");
        Item espelho = new Item("espelho", "você encontrou um espelho");
        Item bau = new Item("bau", "você encontrou um bau com entradas para 3 chaves.");

        cozinha.addItem(copo);
        armas.addItem(espada);
        quarto.addItem(roupas);
        torre.addItem(chave);
        quintal.addItem(chave);
        quarto.addItem(chave);
        banheiro.addItem(espelho);
        sala.addItem(bau);
    }

    public void play() 
    {            
        printWelcome();
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Obrigado por jogar. Até a próxima!");
    }

    private void printWelcome()
    {
        System.out.println();
        System.out.println("Bem vindo ao Castelo - O Jogo.");
        System.out.println("Um jogo de aventura novo e incrivelmente chato.");
        System.out.println("Digite ajuda para saber mais.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    

    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("Não sei o que você quer dizer...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("ajuda")) {
            printHelp();
        }
        else if (commandWord.equals("caminhar")) {
            goRoom(command);
        }
        else if (commandWord.equals("sair")) {
            wantToQuit = quit(command);
        }
        else if(commandWord.equals("averiguar")){
            System.out.println("Você está " + currentRoom.getShortDescription() + ".");
            List<Item> items = currentRoom.getItems();
            if(items.isEmpty()) {
                System.out.println("A sala não possui itens");
            }
            else {
                List<Item> itemsToRemove = new ArrayList<>();
                System.out.println("Você vê os seguintes itens na sala:");
                for(Item item : items) {
                    System.out.println("- " + item.getName() + ": " + item.getDescription());
                    if(item.getName().equals("chave")) {
                        int chavesEncontradas = getChavesEncontradas();
                        setChavesEncontradas(chavesEncontradas + 1);  // incrementa o número de chaves encontradas.
                        itemsToRemove.add(item);
                    }
                }   
                items.removeAll(itemsToRemove);
            }
        }
        else if (commandWord.equals("abrir")) {
            if (currentRoom.getName().equals("sala")) {
                if (chavesEncontradas == 3) {
                    System.out.println("Parabéns, você conseguiu abrir o bau e encontrar o tesouro do castelo!");
                    wantToQuit = true;
                }
                else {
                    System.out.println("Você precisa encontrar todas as chaves para abrir esta porta.");
                }
            }
            else {
                System.out.println("Não há nada para abrir aqui");
            }
        }
        return wantToQuit;
    }

    // implementations of user commands:


    public int getChavesEncontradas() 
    {
        return chavesEncontradas;
    }

    public void setChavesEncontradas(int chavesEncontradas) 
    {
        this.chavesEncontradas = chavesEncontradas;
    }


    private void printHelp() 
    {
        System.out.println("Você está sozinho neste castelo.");
        System.out.println("Precisa procurar o tesouro que veio encontrar.");
        System.out.println();
        System.out.println("Seus comandos são:");
        parser.showCommands();
    }

    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Para onde?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("Não tem saída para este lado.");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
        System.out.println("Chaves encontradas: " + getChavesEncontradas());
    }

    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Sair do quê?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
