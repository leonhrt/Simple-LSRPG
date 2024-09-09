import business.manager.adventure.AdventureFacade;
import business.manager.adventure.AdventureManager;
import business.manager.character.CharacterFacade;
import business.manager.character.CharacterManager;
import business.manager.monster.MonsterFacade;
import business.manager.monster.MonsterManager;
import persistence.exceptions.PersistenceException;
import presentation.Controller;
import presentation.Menu;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();

        CharacterFacade characterManager = new CharacterManager();
        AdventureFacade adventureManager = new AdventureManager();
        MonsterFacade monsterManager = new MonsterManager();

        Controller controller = new Controller(characterManager, adventureManager, monsterManager, menu);

        try {
            if (controller.askPersistenceSource()) {
                controller.run();
            }

        } catch (PersistenceException e) {
            menu.showMessage(e.getMessage());
        }
    }
}