package com.example.kimthanhphatmvc.controller.admin;

import com.example.kimthanhphatmvc.model.enums.InventoryActionType;
import com.example.kimthanhphatmvc.model.enums.InventoryExportReason;
import com.example.kimthanhphatmvc.model.inventory.*;
import com.example.kimthanhphatmvc.repository.ProductRepository;
import com.example.kimthanhphatmvc.repository.inventory.*;
import com.example.kimthanhphatmvc.service.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/inventory")
@RequiredArgsConstructor
public class InventoryAdminController {

    // ===== REPOSITORY =====
    private final InventoryExportRepository inventoryExportRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final InventoryStockRepository inventoryStockRepository;
    private final InventoryImportRepository importRepo;
    private final InventoryImportItemRepository itemRepo;
    private final ProductRepository productRepository;
    private final InventoryExportItemRepository inventoryExportItemRepository;

    // ===== SERVICE =====
    private final InventoryService inventoryService;

    /* ================= DASHBOARD ================= */

    @GetMapping
    public String inventoryDashboard(Model model) {

        model.addAttribute("pageTitle", "Dashboard Kho");
        model.addAttribute("content", "admin/inventory/dashboard");

        long totalProductsInStock = inventoryStockRepository.count();
        Integer totalQuantity = inventoryStockRepository.sumQuantity();

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(23, 59, 59);

        long importToday = inventoryMovementRepository
                .countByActionTypeAndActionDateBetween(
                        InventoryActionType.IMPORT, start, end);

        long exportToday = inventoryMovementRepository
                .countByActionTypeAndActionDateBetween(
                        InventoryActionType.EXPORT, start, end);

        model.addAttribute("topHighStocks",
                inventoryStockRepository.findTop5ByOrderByQuantityDesc());
        model.addAttribute("topLowStocks",
                inventoryStockRepository.findTop5ByOrderByQuantityAsc());

        model.addAttribute("totalProductsInStock", totalProductsInStock);
        model.addAttribute("totalQuantity", totalQuantity == null ? 0 : totalQuantity);
        model.addAttribute("importToday", importToday);
        model.addAttribute("exportToday", exportToday);

        return "admin/layout";
    }

    /* ================= NHẬP KHO ================= */

    @GetMapping("/import")
    public String showImportForm(Model model) {

        model.addAttribute("pageTitle", "Nhập kho");
        model.addAttribute("content", "admin/inventory/import");
        model.addAttribute("inventoryImport", new InventoryImport());
        model.addAttribute("products", productRepository.findAll());

        return "admin/layout";
    }

    @PostMapping("/import")
    public String importStock(@ModelAttribute InventoryImport inventoryImport) {
        inventoryService.importStock(inventoryImport);
        return "redirect:/admin/inventory/import?success";
    }

    /* ================= CHI TIẾT PHIẾU NHẬP ================= */

    @GetMapping("/import/{id}")
    public String importDetail(@PathVariable Long id, Model model) {

        InventoryImport importEntity = importRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập #" + id));

        List<InventoryImportItem> items =
                itemRepo.findByInventoryImportId(id);

        model.addAttribute("pageTitle", "Chi tiết phiếu nhập #" + id);
        model.addAttribute("content", "admin/inventory/import-detail");
        model.addAttribute("inventoryImport", importEntity);
        model.addAttribute("items", items);

        return "admin/layout";
    }

    /* ================= XUẤT KHO ================= */

    @GetMapping("/export")
    public String showExportForm(Model model) {

        model.addAttribute("pageTitle", "Xuất kho");
        model.addAttribute("content", "admin/inventory/export");
        model.addAttribute("inventoryExport", new InventoryExport());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("reasons", InventoryExportReason.values());

        // ✅ QUAN TRỌNG: danh sách tồn kho > 0
        model.addAttribute(
                "stocks",
                inventoryStockRepository.findByQuantityGreaterThan(0)
        );

        return "admin/layout";
    }


    @PostMapping("/export")
    public String exportStock(@ModelAttribute InventoryExport inventoryExport) {
        inventoryService.exportStock(inventoryExport);
        return "redirect:/admin/inventory/export?success";
    }

    /* ================= CHI TIẾT PHIẾU XUẤT ================= */

    @GetMapping("/export/{id}")
    public String exportDetail(@PathVariable Long id, Model model) {

        InventoryExport inventoryExport = inventoryExportRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu xuất"));

        List<InventoryExportItem> items =
                inventoryExportItemRepository.findByInventoryExportId(id);

        model.addAttribute("pageTitle", "Chi tiết phiếu xuất");
        model.addAttribute("content", "admin/inventory/export-detail");
        model.addAttribute("export", inventoryExport);
        model.addAttribute("items", items);

        return "admin/layout";
    }



    /* ================= TỒN KHO ================= */

    @GetMapping("/stocks")
    public String viewStocks(Model model,
                             @RequestParam(value = "keyword", required = false) String keyword) {

        List<InventoryStock> stocks;

        if (keyword != null && !keyword.trim().isEmpty()) {
            stocks = inventoryStockRepository
                    .findByProductNameContainingIgnoreCase(keyword);
        } else {
            stocks = inventoryStockRepository.findAll();
        }

        int totalQuantity = stocks.stream()
                .mapToInt(InventoryStock::getQuantity)
                .sum();

        model.addAttribute("stocks", stocks);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalQuantity", totalQuantity);

        model.addAttribute("pageTitle", "Tồn kho");
        model.addAttribute("content", "admin/inventory/stocks");

        return "admin/layout";
    }

    /* ================= LỊCH SỬ KHO ================= */

    @GetMapping("/movements")
    public String viewMovements(
            @RequestParam(value = "productId", required = false) Long productId,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to,
            Model model) {

        LocalDateTime fromDate = (from != null && !from.isBlank())
                ? LocalDate.parse(from).atStartOfDay()
                : null;

        LocalDateTime toDate = (to != null && !to.isBlank())
                ? LocalDate.parse(to).atTime(23, 59, 59)
                : null;

        List<InventoryMovement> movements =
                inventoryMovementRepository.filterMovements(
                        productId,
                        fromDate,
                        toDate
                );

        model.addAttribute("movements", movements);
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("productId", productId);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        model.addAttribute("pageTitle", "Lịch sử kho");
        model.addAttribute("content", "admin/inventory/movements");

        return "admin/layout";
    }

    /* ================= BÁO CÁO ================= */

    @GetMapping("/report")
    public String inventoryReport(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to,

            Model model
    ) {

        LocalDateTime fromDate = (from != null)
                ? from.atStartOfDay()
                : LocalDate.now().minusDays(7).atStartOfDay();

        LocalDateTime toDate = (to != null)
                ? to.atTime(23, 59, 59)
                : LocalDate.now().atTime(23, 59, 59);

        List<InventoryMovement> movements =
                inventoryMovementRepository
                        .findByActionDateBetweenOrderByActionDateAsc(
                                fromDate, toDate
                        );

        int totalImport = movements.stream()
                .filter(m -> m.getActionType() == InventoryActionType.IMPORT)
                .mapToInt(InventoryMovement::getQuantityChange)
                .sum();

        int totalExport = movements.stream()
                .filter(m -> m.getActionType() == InventoryActionType.EXPORT)
                .mapToInt(m -> Math.abs(m.getQuantityChange()))
                .sum();

        model.addAttribute("pageTitle", "Báo cáo kho");
        model.addAttribute("content", "admin/inventory/report");
        model.addAttribute("movements", movements);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("totalImport", totalImport);
        model.addAttribute("totalExport", totalExport);

        return "admin/layout";
    }
}
