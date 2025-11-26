
    /* Toggle từng khối danh mục */
    function toggleSB(titleEl) {
    titleEl.classList.toggle("active");
    const content = titleEl.nextElementSibling;
    content.classList.toggle("open");
}

    /* Show more (refactor tối ưu) */
    function toggleShowMore(type) {
    const btn = document.getElementById(`${type}-btn`);
    const hiddenItems = document.querySelectorAll(`.${type}-hidden`);

    // Tránh lỗi khi không có nút
    if (!btn || hiddenItems.length === 0) {
    console.warn(`Không tìm thấy show-more cho: ${type}`);
    return;
}

    const isExpanding = btn.getAttribute("data-expand") !== "true";

    hiddenItems.forEach(el => el.classList.toggle("d-none"));

    // Cập nhật trạng thái
    btn.setAttribute("data-expand", isExpanding ? "true" : "false");
    btn.innerText = isExpanding ? "Thu gọn" : "Xem thêm";
}
