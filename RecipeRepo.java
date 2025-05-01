import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class RecipeRepo {

    private static final String DB_URL = "jdbc:sqlite:myrecipes.db";


    //Make sure you can connect into database
    public static void connect(Boolean isSave, String recipeType, String recipeTitle, BufferedImage recipeImage, String recipeDescription, String recipeInstructions,
                               HashMap<String, IngredientSize> ingredients) {
        try( Connection conn = DriverManager.getConnection(DB_URL)) {

            createTableIfNotExists(conn);
            if(isSave) {
                save(conn, recipeTitle, recipeType, recipeImage, recipeDescription, recipeInstructions, ingredients);
            }
            else {
                load(conn);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    public static void enableWALMode(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL");
        }
    }

    //Create the database table if the table is not yet created
    private static void createTableIfNotExists(Connection conn) throws SQLException {

        //Tables for Recipe and Ingredient Entities
        String sql = """
                CREATE TABLE IF NOT EXISTS recipes (
                    recipe_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    recipe_type TEXT NOT NULL,
                    recipe_name TEXT NOT NULL,
                    recipe_image BLOB,
                    recipe_description TEXT NOT NULL,
                    recipe_instructions TEXT NOT NULL
                );
        """;
        String sql2 = """
                CREATE TABLE IF NOT EXISTS ingredients (
                    ingredient_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ingredient_name TEXT NOT NULL,
                    ingredient_amount TEXT NOT NULL,
                    recipe_id INTEGER,
                    FOREIGN KEY(recipe_id) REFERENCES recipes(recipe_id)
                );
        """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            stmt.executeUpdate(sql2);
        }
    }


    //Save recipe data to database
    private static void save(Connection conn, String recipeName, String recipeType, BufferedImage recipeImage, String description, String instructions,
                             HashMap<String, IngredientSize> ingredients) throws SQLException {
        String sqlRecipe = "INSERT INTO recipes (recipe_name, recipe_type, recipe_image, recipe_description, recipe_instructions) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement recipeStatement = conn.prepareStatement(sqlRecipe, Statement.RETURN_GENERATED_KEYS)) {
            recipeStatement.setString(1, recipeName);
            recipeStatement.setString(2, recipeType);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(recipeImage, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            recipeStatement.setBytes(3, imageBytes);
            recipeStatement.setString(4, description);
            recipeStatement.setString(5, instructions);


            recipeStatement.executeUpdate();

            ResultSet keys = recipeStatement.getGeneratedKeys();
            if (keys.next()) {
                int recipeId = keys.getInt(1);

                String sqlIngredient = "INSERT INTO ingredients (ingredient_name, ingredient_amount, recipe_id) VALUES (?, ?, ?)";
                try (PreparedStatement ingredientStatement = conn.prepareStatement(sqlIngredient)) {
                    for (Map.Entry<String, IngredientSize> ingredientData : ingredients.entrySet()) {
                        ingredientStatement.setString(1, ingredientData.getKey());
                        ingredientStatement.setString(2, ingredientData.getValue().toString());
                        ingredientStatement.setInt(3, recipeId);
                        ingredientStatement.executeUpdate();
                    }
                    System.out.println("Ingredients added successfully");
                }
            }
            System.out.println("Recipes added successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //Load recipe data to database
    private static ArrayList<Recipe> load(Connection conn) throws SQLException {
        ArrayList<Recipe> recipeList = new ArrayList<>();

        String sql = "SELECT * FROM recipes";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int recipeId = rs.getInt("recipe_id");
                String type = rs.getString("recipe_type");
                String title = rs.getString("recipe_name");
                byte[] image = rs.getBytes("recipe_image");
                String description = rs.getString("recipe_description");
                String instructions = rs.getString("recipe_instructions");

                BufferedImage recipeImage = null;
                if (image != null) {
                    try {
                        recipeImage = ImageIO.read(new ByteArrayInputStream(image));
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                }

                HashMap<String, IngredientSize> ingredients = new HashMap<>();
                String sqlIngredient = "SELECT * FROM ingredients WHERE recipe_id = ?";
                try (PreparedStatement ingredientStatement = conn.prepareStatement(sqlIngredient)) {
                    ingredientStatement.setInt(1, recipeId);
                    try (ResultSet rs2 = ingredientStatement.executeQuery()) {
                        while (rs2.next()) {
                            String ingredientName = rs2.getString("ingredient_name");
                            String ingredientAmount = rs2.getString("ingredient_amount");
                            IngredientSize ingredientSize = IngredientSize.fromString(ingredientAmount);
                            ingredients.put(ingredientName, ingredientSize);
                        }
                    }
                }

                Recipe recipe = new Recipe(title, type, description, recipeImage, instructions, ingredients);
                recipe.setId(recipeId);

                recipeList.add(recipe);
            }
        }
        return recipeList;
    }
}
