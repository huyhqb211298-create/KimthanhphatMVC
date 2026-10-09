document.addEventListener("DOMContentLoaded", () => {
    const marquee = document.querySelector("[data-partner-marquee]");
    const previousButton = document.querySelector("[data-partner-prev]");
    const nextButton = document.querySelector("[data-partner-next]");
    const firstGroup = marquee?.querySelector(".home-partner-group");

    if (!marquee || !firstGroup || !previousButton || !nextButton) return;

    const reducedMotion = window.matchMedia("(prefers-reduced-motion: reduce)");
    let paused = false;
    let animationFrame;
    let lastTime = 0;

    const groupWidth = () => firstGroup.getBoundingClientRect().width;
    const itemStep = () => {
        const item = firstGroup.querySelector(".home-partner-item");
        if (!item) return 200;
        const styles = window.getComputedStyle(item);
        return item.getBoundingClientRect().width + parseFloat(styles.marginRight || "0");
    };

    const normalizePosition = () => {
        if (reducedMotion.matches) return;
        const width = groupWidth();
        if (!width) return;
        if (marquee.scrollLeft <= 1) marquee.scrollLeft += width;
        if (marquee.scrollLeft >= width * 2 - marquee.clientWidth - 1) marquee.scrollLeft -= width;
    };

    const setInitialPosition = () => {
        const width = groupWidth();
        if (width) marquee.scrollLeft = reducedMotion.matches ? 0 : width;
    };

    const move = (direction) => {
        normalizePosition();
        marquee.scrollBy({ left: direction * itemStep(), behavior: "smooth" });
        window.setTimeout(normalizePosition, 500);
    };

    const tick = (time) => {
        if (!paused && !reducedMotion.matches && lastTime) {
            marquee.scrollLeft -= Math.min(time - lastTime, 32) * 0.025;
            normalizePosition();
        }
        lastTime = time;
        animationFrame = window.requestAnimationFrame(tick);
    };

    previousButton.addEventListener("click", () => move(-1));
    nextButton.addEventListener("click", () => move(1));
    marquee.addEventListener("mouseenter", () => { paused = true; });
    marquee.addEventListener("mouseleave", () => { paused = false; });
    marquee.addEventListener("focusin", () => { paused = true; });
    marquee.addEventListener("focusout", () => { paused = false; });
    marquee.addEventListener("pointerdown", () => { paused = true; });
    marquee.addEventListener("pointerup", () => { paused = false; });
    window.addEventListener("resize", setInitialPosition);
    reducedMotion.addEventListener("change", setInitialPosition);

    setInitialPosition();
    animationFrame = window.requestAnimationFrame(tick);
    window.addEventListener("pagehide", () => window.cancelAnimationFrame(animationFrame), { once: true });
});
