package com.restaurant.config;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import com.restaurant.util.QRCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final TableRepository tableRepository;
    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final AccountRepository accountRepository;
    private final TaxRepository taxRepository;
    private final InventoryRepository inventoryRepository;
    private final StaffRepository staffRepository;
    private final ShiftRepository shiftRepository;
    private final CustomerRepository customerRepository;
    private final RiderRepository riderRepository;
    private final SystemSettingRepository systemSettingRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public void run(String... args) {
        try {
            long userCount = userRepository.count();
            long menuItemCount = menuItemRepository.count();
            long tableCount = tableRepository.count();
            long categoryCount = categoryRepository.count();
            log.info("Current counts: users={}, menuItems={}, tables={}, categories={}",
                userCount, menuItemCount, tableCount, categoryCount);

            // Always check each collection independently so partial data loss gets repaired
            log.info("Checking and seeding missing data...");
            if (branchRepository.count() == 0) { log.info("Seeding branches..."); seedBranches(); }
            if (userCount == 0) { log.info("Seeding users..."); seedUsers(); }
            
            if (userRepository.findByEmail("joe@gmail.com").isEmpty()) {
                log.info("Seeding customer demo user...");
                User customer = User.builder()
                    .name("Regular Joe")
                    .email("joe@gmail.com")
                    .password(passwordEncoder.encode("customer123"))
                    .role("CUSTOMER")
                    .branch("Main Branch")
                    .status("active")
                    .createdAt(LocalDateTime.now())
                    .build();
                userRepository.save(customer);
            }
            if (tableCount < 5) { log.info("Seeding tables..."); tableRepository.deleteAll(); seedTables(); }
            if (inventoryRepository.count() < 5) { log.info("Seeding inventory..."); inventoryRepository.deleteAll(); seedInventory(); }
            if (categoryCount < 5) { log.info("Seeding categories..."); categoryRepository.deleteAll(); seedCategories(); }
            if (menuItemCount < 5) { log.info("Seeding menu items..."); menuItemRepository.deleteAll(); seedMenuItems(); }
            if (staffRepository.count() < 4) { log.info("Seeding staff..."); staffRepository.deleteAll(); seedStaff(); }
            if (customerRepository.count() < 4) { log.info("Seeding customers..."); customerRepository.deleteAll(); seedCustomers(); }
            if (riderRepository.count() == 0) { log.info("Seeding riders..."); seedRiders(); }
            if (accountRepository.count() < 5) { log.info("Seeding chart of accounts..."); accountRepository.deleteAll(); seedChartOfAccounts(); }
            if (taxRepository.count() == 0) { log.info("Seeding taxes..."); seedTaxes(); }
            if (systemSettingRepository.findByKey("currency_symbol").isEmpty()) {
                log.info("Seeding default settings...");
                seedSettings();
            }
            log.info("Data check complete! users={}, menuItems={}, tables={}, categories={}",
                userRepository.count(), menuItemRepository.count(),
                tableRepository.count(), categoryRepository.count());
        } catch (Exception e) {
            log.error("Error during data initialization: {}", e.getMessage(), e);
        }
    }

    private void seedBranches() {
        Branch main = Branch.builder()
            .name("Main Branch")
            .address("123 Restaurant Street, City Center")
            .phone("+1-555-0100")
            .email("main@restaurant.com")
            .managerName("System Admin")
            .status("active")
            .build();
        branchRepository.save(main);
    }

    private void seedUsers() {
        User admin = User.builder()
            .name("System Admin")
            .email("admin@restaurant.com")
            .password(passwordEncoder.encode("admin123"))
            .role("ADMIN")
            .branch("Main Branch")
            .status("active")
            .createdAt(LocalDateTime.now())
            .build();

        User manager = User.builder()
            .name("John Manager")
            .email("manager@restaurant.com")
            .password(passwordEncoder.encode("manager123"))
            .role("MANAGER")
            .branch("Main Branch")
            .status("active")
            .createdAt(LocalDateTime.now())
            .build();

        User staff = User.builder()
            .name("Jane Staff")
            .email("staff@restaurant.com")
            .password(passwordEncoder.encode("staff123"))
            .role("STAFF")
            .branch("Main Branch")
            .status("active")
            .createdAt(LocalDateTime.now())
            .build();

        User customer = User.builder()
            .name("Regular Joe")
            .email("joe@gmail.com")
            .password(passwordEncoder.encode("customer123"))
            .role("CUSTOMER")
            .branch("Main Branch")
            .status("active")
            .createdAt(LocalDateTime.now())
            .build();

        userRepository.saveAll(List.of(admin, manager, staff, customer));
    }

    private void seedTables() {
        String[][] tableData = {
            {"T1", "4", "Indoor"}, {"T2", "2", "Indoor"}, {"T3", "6", "Outdoor"},
            {"T4", "8", "VIP"}, {"T5", "4", "Indoor"}
        };

        for (String[] t : tableData) {
            String qrUrl = baseUrl + "/menu/public?table=" + t[0];
            String qrCode = QRCodeUtil.generateQRCodeBase64(qrUrl, 200, 200);
            RestaurantTable table = RestaurantTable.builder()
                .tableNumber(t[0])
                .capacity(Integer.parseInt(t[1]))
                .location(t[2])
                .status("Available")
                .branch("Main Branch")
                .qrUrl(qrUrl)
                .qrCode(qrCode)
                .build();
            tableRepository.save(table);
        }
    }

    private void seedCategories() {
        String[][] cats = {
            {"Starters", "Appetizers and small bites", "1"},
            {"Main Course", "Primary dishes and entrees", "2"},
            {"Grills & BBQ", "Grilled meats and BBQ specialties", "3"},
            {"Seafood", "Fresh fish and seafood dishes", "4"},
            {"Pasta & Rice", "Italian pasta and rice dishes", "5"},
            {"Burgers", "Gourmet burgers and sandwiches", "6"},
            {"Pizzas", "Wood-fired and classic pizzas", "7"},
            {"Salads", "Fresh salads and healthy options", "8"},
            {"Desserts", "Sweet treats and desserts", "9"},
            {"Beverages", "Drinks, juices and beverages", "10"}
        };

        for (String[] c : cats) {
            Category cat = Category.builder()
                .name(c[0])
                .description(c[1])
                .sortOrder(Integer.parseInt(c[2]))
                .active(true)
                .branch("Main Branch")
                .imageUrl("https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=200")
                .build();
            categoryRepository.save(cat);
        }
    }

    private void seedMenuItems() {
        List<Category> categories = categoryRepository.findAll();

        String[][] items = {
            {"Crispy Spring Rolls", "3", "8.99", "https://images.unsplash.com/photo-1544025162-d76694265947?w=300"},
            {"Chicken Wings", "3", "12.99", "https://images.unsplash.com/photo-1527477396000-e27163b481c2?w=300"},
            {"Grilled Salmon", "4", "24.99", "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=300"},
            {"Beef Steak", "2", "34.99", "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=300"},
            {"Margherita Pizza", "6", "16.99", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=300"},
            {"Caesar Salad", "7", "10.99", "https://images.unsplash.com/photo-1550304943-4f24f54ddde9?w=300"},
            {"Chocolate Lava Cake", "8", "7.99", "https://images.unsplash.com/photo-1606313564200-e75d5e30476c?w=300"},
            {"Fresh Lemonade", "9", "4.99", "https://images.unsplash.com/photo-1621263764928-df1444c5e859?w=300"},
            {"Beef Burger", "5", "14.99", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=300"},
            {"Pasta Carbonara", "4", "15.99", "https://images.unsplash.com/photo-1612874742237-6526221588e3?w=300"},
            {"Garlic Bread", "0", "5.99", "https://images.unsplash.com/photo-1573140247632-f8fd74997d5c?w=300"},
            {"Tiramisu", "8", "8.99", "https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=300"}
        };

        List<Inventory> inventoryList = inventoryRepository.findAll();
        for (int i = 0; i < items.length; i++) {
            String[] item = items[i];
            int catIndex = Integer.parseInt(item[1]);
            String catId = (categories != null && !categories.isEmpty()) ? 
                (catIndex < categories.size() ? categories.get(catIndex).getId() : categories.get(0).getId()) : null;
            String catName = (categories != null && !categories.isEmpty()) ? 
                (catIndex < categories.size() ? categories.get(catIndex).getName() : categories.get(0).getName()) : "Uncategorized";

            MenuItem mi = MenuItem.builder()
                .name(item[0])
                .categoryId(catId)
                .categoryName(catName)
                .price(Double.parseDouble(item[2]))
                .imageUrl(item[3])
                .description("Delicious " + item[0] + " prepared with fresh ingredients")
                .available(true)
                .branch("Main Branch")
                .preparationTime(15)
                .build();
            
            // Link to inventory product if available
            if (i < inventoryList.size()) {
                mi.setInventoryProductId(inventoryList.get(i).getId());
            }
            
            menuItemRepository.save(mi);
        }
    }

    private void seedChartOfAccounts() {
        List<Account> accounts = new ArrayList<>();
        // Asset Accounts
        accounts.add(buildAccount("1001", "Cash", "Asset", "Current Asset", 5000.0));
        accounts.add(buildAccount("1002", "Bank Account", "Asset", "Current Asset", 25000.0));
        accounts.add(buildAccount("1003", "Accounts Receivable", "Asset", "Current Asset", 0.0));
        accounts.add(buildAccount("1201", "Kitchen Equipment", "Asset", "Fixed Asset", 15000.0));
        accounts.add(buildAccount("1202", "Furniture & Fixtures", "Asset", "Fixed Asset", 8000.0));
        // Liability Accounts
        accounts.add(buildAccount("2001", "Accounts Payable", "Liability", "Current Liability", 0.0));
        accounts.add(buildAccount("2002", "Tax Payable", "Liability", "Current Liability", 0.0));
        accounts.add(buildAccount("2003", "Salaries Payable", "Liability", "Current Liability", 0.0));
        // Equity Accounts
        accounts.add(buildAccount("3001", "Owner's Equity", "Equity", "Capital", 50000.0));
        accounts.add(buildAccount("3002", "Retained Earnings", "Equity", "Retained Earnings", 0.0));
        // Revenue Accounts
        accounts.add(buildAccount("4001", "Food Sales", "Revenue", "Sales", 0.0));
        accounts.add(buildAccount("4002", "Beverage Sales", "Revenue", "Sales", 0.0));
        accounts.add(buildAccount("4003", "Delivery Charges", "Revenue", "Other Revenue", 0.0));
        // Expense Accounts
        accounts.add(buildAccount("5001", "Cost of Goods Sold", "Expense", "COGS", 0.0));
        accounts.add(buildAccount("5002", "Salaries & Wages", "Expense", "Operating Expense", 0.0));
        accounts.add(buildAccount("5003", "Rent Expense", "Expense", "Operating Expense", 0.0));
        accounts.add(buildAccount("5004", "Utilities Expense", "Expense", "Operating Expense", 0.0));
        accounts.add(buildAccount("5005", "Marketing Expense", "Expense", "Operating Expense", 0.0));
        accounts.add(buildAccount("5006", "Maintenance Expense", "Expense", "Operating Expense", 0.0));

        accountRepository.saveAll(accounts);
    }

    private Account buildAccount(String number, String name, String type, String category, Double balance) {
        return Account.builder()
            .accountNumber(number)
            .name(name)
            .type(type)
            .category(category)
            .openingBalance(balance)
            .currentBalance(balance)
            .totalDebit(0.0)
            .totalCredit(0.0)
            .status("active")
            .isSystem(true)
            .branch("Main Branch")
            .build();
    }

    private void seedTaxes() {
        Tax vat = Tax.builder()
            .name("VAT")
            .rate(10.0)
            .type("VAT")
            .appliesTo(Arrays.asList("all"))
            .status("active")
            .build();
        taxRepository.save(vat);
    }

    private void seedStaff() {
        String[][] staffData = {
            {"Admin User", "admin@restaurant.com", "Waiter", "Service", "2500.00"},
            {"Chef Mario", "mario@restaurant.com", "Chef", "Kitchen", "4500.00"},
            {"Jane Waiter", "jane@restaurant.com", "Waiter", "Service", "2200.00"},
            {"Bob Cashier", "bob@restaurant.com", "Cashier", "Front Desk", "2800.00"}
        };
        for (String[] s : staffData) {
            Staff st = Staff.builder()
                .name(s[0]).email(s[1]).position(s[2]).department(s[3]).salary(Double.parseDouble(s[4]))
                .status("active").employeeNumber("EMP-" + (1000 + staffRepository.count())).branch("Main Branch").build();
            staffRepository.save(st);
            
            // Seed a shift
            Shift shift = Shift.builder().employeeId(st.getId()).employeeName(st.getName()).date(LocalDate.now())
                .startTime(java.time.LocalTime.of(9, 0)).endTime(java.time.LocalTime.of(17, 0)).status("Scheduled").branch("Main Branch").build();
            shiftRepository.save(shift);
        }
    }

    private void seedCustomers() {
        Customer c = Customer.builder()
            .name("Walk-in Customer").phone("0000000000").email("walkin@restaurant.com")
            .loyaltyPoints(0).branch("Main Branch").status("active").build();
        customerRepository.save(c);
        
        Customer c2 = Customer.builder()
            .name("Regular Joe").phone("0771234567").email("joe@gmail.com")
            .loyaltyPoints(150).branch("Main Branch").status("active").build();
        customerRepository.save(c2);

        Customer c3 = Customer.builder()
            .name("Alice Smith").phone("0779876543").email("alice@example.com")
            .loyaltyPoints(420).branch("Main Branch").status("active").build();
        customerRepository.save(c3);

        Customer c4 = Customer.builder()
            .name("Bob Johnson").phone("0771122334").email("bob@example.com")
            .loyaltyPoints(50).branch("Main Branch").status("active").build();
        customerRepository.save(c4);
    }

    private void seedRiders() {
        Rider r1 = Rider.builder()
            .name("Speedy Gonzalez").phone("0775551111").vehicleType("Motorcycle")
            .vehicleNumber("BXY-1234").licenseNumber("LIC-998877").salary(3000.0)
            .status("active").createdAt(LocalDateTime.now()).build();
        riderRepository.save(r1);

        Rider r2 = Rider.builder()
            .name("Flash Gordon").phone("0775552222").vehicleType("Bicycle")
            .vehicleNumber("N/A").licenseNumber("N/A").salary(1500.0)
            .status("active").createdAt(LocalDateTime.now()).build();
        riderRepository.save(r2);
    }

    private void seedInventory() {
        String[][] products = {
            {"Chicken Breast", "10.0", "kg", "2.0", "50.0", "8.5"},
            {"Beef Tenderloin", "5.0", "kg", "1.0", "20.0", "22.0"},
            {"Salmon Fillet", "4.0", "kg", "1.0", "15.0", "18.0"},
            {"Pasta", "8.0", "kg", "2.0", "30.0", "2.5"},
            {"Tomato Sauce", "12.0", "liters", "3.0", "40.0", "3.0"},
            {"Cheese", "6.0", "kg", "2.0", "20.0", "12.0"},
            {"Lettuce", "5.0", "kg", "2.0", "20.0", "2.0"},
            {"Cooking Oil", "15.0", "liters", "3.0", "50.0", "4.0"},
            {"Flour", "20.0", "kg", "5.0", "100.0", "1.5"},
            {"Sugar", "10.0", "kg", "3.0", "50.0", "1.2"}
        };

        for (String[] p : products) {
            Inventory inv = Inventory.builder()
                .productName(p[0])
                .quantity(Double.parseDouble(p[1]))
                .unit(p[2])
                .minStock(Double.parseDouble(p[3]))
                .maxStock(Double.parseDouble(p[4]))
                .costPrice(Double.parseDouble(p[5]))
                .lastUpdated(LocalDateTime.now())
                .branch("Main Branch")
                .build();
            inventoryRepository.save(inv);
        }
    }

    private void seedSettings() {
        systemSettingRepository.save(SystemSetting.builder().key("currency_symbol").value("Rs.").build());
        systemSettingRepository.save(SystemSetting.builder().key("currency_name").value("LKR").build());
        systemSettingRepository.save(SystemSetting.builder().key("restaurant_name").value("RestaurantPro").build());
    }
}
