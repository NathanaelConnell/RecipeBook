import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

class RecipeRepo {

    private static final String DB_URL = "jdbc:sqlite:recipes.db";

    //Make sure you can connect into database
    public static void connect(String recipeTitle, BufferedImage recipeImage, String recipeDescription, String recipeInstructions,
                               HashMap<String, IngredientSize> ingredients) {
        try( Connection conn = DriverManager.getConnection(DB_URL)) {

            createTableIfNotExists(conn);
            save(conn, recipeTitle, recipeImage, recipeDescription, recipeInstructions, ingredients);
            load(conn);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    //Create the database table if the table is not yet created
    private static void createTableIfNotExists(Connection conn) throws SQLException {

        //Tables for Recipe and Ingredient Entities
        String sql = """
                CREATE TABLE IF NOT EXISTS recipes (
                    recipe_id INTEGER PRIMARY KEY AUTOINCREMENT,
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
    private static void save(Connection conn, String recipeName, BufferedImage recipeImage, String description, String instructions,
                             HashMap<String, IngredientSize> ingredients) throws SQLException {
        String sqlRecipe = "INSERT INTO recipes (recipe_name, recipe_image, recipe_description, recipe_instructions) VALUES (?, ?, ?, ?)";
        try (PreparedStatement recipeStatement = conn.prepareStatement(sqlRecipe, Statement.RETURN_GENERATED_KEYS)) {
            recipeStatement.setString(1, recipeName);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(recipeImage, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            recipeStatement.setBytes(2, imageBytes);
            recipeStatement.setString(3, description);
            recipeStatement.setString(4, instructions);


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
    private static void load(Connection conn) throws SQLException {
        String sql = "SELECT * FROM recipes";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println("Recipe: " + rs.getString("recipe_name"));
            }
        }
    }
}