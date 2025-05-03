import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class RecipeRepo {

    private static final String DB_URL = "jdbc:sqlite:recipes.db";

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
    public static void save(Recipe recipe) throws SQLException {

        try( Connection conn = DriverManager.getConnection(DB_URL)) {

            createTableIfNotExists(conn);
            String sqlRecipe = "INSERT INTO recipes (recipe_name, recipe_type, recipe_image, recipe_description, recipe_instructions) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement recipeStatement = conn.prepareStatement(sqlRecipe, Statement.RETURN_GENERATED_KEYS)) {
                recipeStatement.setString(1, recipe.getTitle());
                recipeStatement.setString(2, recipe.getType());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(recipe.getImage(), "png", outputStream);
                byte[] imageBytes = outputStream.toByteArray();
                recipeStatement.setBytes(3, imageBytes);
                recipeStatement.setString(4, recipe.getDescription());
                recipeStatement.setString(5, recipe.getInstructions());


                recipeStatement.executeUpdate();

                ResultSet keys = recipeStatement.getGeneratedKeys();
                if (keys.next()) {
                    int recipeId = keys.getInt(1);

                    String sqlIngredient = "INSERT INTO ingredients (ingredient_name, ingredient_amount, recipe_id) VALUES (?, ?, ?)";
                    try (PreparedStatement ingredientStatement = conn.prepareStatement(sqlIngredient)) {
                        for (Map.Entry<String, IngredientSize> ingredientData : recipe.getIngredients().entrySet()) {
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
                throw new SQLException(e);
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    //Load recipe data to database
    public static ArrayList<Recipe> load() throws SQLException {
        ArrayList<Recipe> recipeList = new ArrayList<>();
        try( Connection conn = DriverManager.getConnection(DB_URL)) {
            createTableIfNotExists(conn);
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
                            throw new IOException(e);
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

                    Recipe recipe = new Recipe(recipeId, title, type, description, recipeImage, instructions, ingredients);

                    recipeList.add(recipe);
                }
            }
        } catch (SQLException | IOException e) {
            throw new SQLException(e);
        }
        return recipeList;
    }

    public static void remove(Recipe recipe) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            createTableIfNotExists(conn);
            String sql = "DELETE FROM recipes WHERE recipe_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, recipe.getId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }


}
