# Produktová Aplikace

## Popis

Tato aplikace umožňuje správu produktů v databázi. Poskytuje uživatelské rozhraní pro přidávání, mazání a zobrazení produktů. Kromě toho podporuje import produktů z CSV souboru a kontroluje, zda existují kategorie pro produkty, které se přidávají.

Aplikace využívá JavaFX pro grafické uživatelské rozhraní a pracuje s databází pomocí JDBC.

## Funkce

1. **Přidání produktu** - Umožňuje přidat nový produkt do databáze. Před přidáním zkontroluje, zda zadaná kategorie existuje.
2. **Mazání produktu** - Umožňuje odstranit produkt z databáze podle jeho ID.
3. **Získání všech produktů** - Zobrazuje seznam všech produktů v databázi.
4. **Import produktů** - Umožňuje importovat produkty z CSV souboru do databáze.
5. **Databázová připojení** - Aplikace načítá připojení k databázi pomocí `config.properties` souboru.

## Požadavky

- **JDK 18 nebo vyšší**
- **JavaFX knihovna** pro uživatelské rozhraní
- **JDBC** pro připojení k databázi
- **OpenCSV** pro práci s CSV soubory

## Instalace

1. Stáhněte nebo klonujte tento repozitář.

   ```bash
   git clone https://github.com/Krataman/dbproject.git

2. Zajistěte, aby vaše databáze byla správně nakonfigurována.
  - Ujistěte se, že máte tabulky Products a Categories v databázi.
  - Konfigurujte připojení k databázi ve souboru config.properties.

3. Vytvořte soubor config.properties v kořenovém adresáři projektu s následujícím obsahem:

```bash
db.url=jdbc:mysql://localhost:3306/nazev_databaze
db.username=uzivatel
db.password=heslo
```
## Použití
Po spuštění aplikace se otevře grafické uživatelské rozhraní s následujícími záložkami:

1. Přidat produkt
Zadejte název, cenu, popis a ID kategorie produktu.
Můžete také importovat produkty z CSV souboru kliknutím na tlačítko "Importovat produkty".
2. Smazat produkt
Zadejte ID produktu, který chcete odstranit, a klikněte na tlačítko "Smazat produkt".
3. Získat produkty
Klikněte na tlačítko "Zobrazit produkty" pro zobrazení všech produktů v databázi.

```bash
src/
├── main/
│   ├── java/
│   │   ├── org/dbprj/dbproject/
│   │   │   ├── ProductApp.java           # Hlavní třída pro JavaFX aplikaci
│   │   │   ├── ProductDAO.java           # Třída pro práci s produkty v databázi
│   │   │   ├── DatabaseConnection.java   # Třída pro připojení k databázi
│   │   │   └── Config.java               # Třída pro načítání konfiguračního souboru
│   └── resources/
│       └── config.properties             # Konfigurační soubor pro připojení k databázi
└── pom.xml                               # Maven konfigurace
```
## Závislosti
JavaFX: Knihovna pro tvorbu GUI aplikace.
OpenCSV: Knihovna pro import/export CSV souborů.
JDBC: Pro připojení a práci s databází.

## Příklady CSV formátu pro import
Formát CSV pro import produktů:

```csv
name,price,description,category_id
Produkt 1,1000,popis 1,1
Produkt 2,199.99,popis 2,2
```








