document.addEventListener("DOMContentLoaded", () => {
    const marquee = document.querySelector("[data-partner-marquee]");
    const previousButton = document.querySelector("[data-partner-prev]");
    const nextButton = document.querySelector("[data-partner-next]");
    const firstGroup = marquee?.querySelector(".home-partner-group");

    if (!marquee || !firstGroup || !previousButton || !nextButton) return;

    const speed = 50 / 1000;
    let animationFrame = 0;
    let lastTime = 0;
    let pauseUntil = 0;
    let pointerActive = false;
    let sectionVisible = true;

    const groupWidth = () => firstGroup.getBoundingClientRect().width;
    const itemStep = () => {
        const item = firstGroup.querySelector(".home-partner-item");
        if (!item) return 200;
        const styles = window.getComputedStyle(item);
        return item.getBoundingClientRect().width + parseFloat(styles.marginRight || "0");
    };

    const normalizePosition = () => {
        const width = groupWidth();
        if (!width) return;

        while (marquee.scrollLeft > width) marquee.scrollLeft -= width;
        while (marquee.scrollLeft <= 0) marquee.scrollLeft += width;
    };

    const pauseBriefly = (duration = 1100) => {
        pauseUntil = performance.now() + duration;
    };

    const move = (direction) => {
        const width = groupWidth();
        if (!width) return;

        normalizePosition();
        if (direction < 0 && marquee.scrollLeft < itemStep()) marquee.scrollLeft += width;
        marquee.scrollBy({ left: direction * itemStep(), behavior: "smooth" });
        pauseBriefly();
        window.setTimeout(normalizePosition, 650);
    };

    const tick = (time) => {
        const elapsed = lastTime ? Math.min(time - lastTime, 50) : 0;

        if (elapsed && sectionVisible && !pointerActive && !document.hidden && time >= pauseUntil) {
            marquee.scrollLeft -= elapsed * speed;
            normalizePosition();
        }

        lastTime = time;
        animationFrame = window.requestAnimationFrame(tick);
    };

    previousButton.addEventListener("click", () => move(-1));
    nextButton.addEventListener("click", () => move(1));

    marquee.addEventListener("pointerdown", () => {
        pointerActive = true;
    });
    ["pointerup", "pointercancel", "pointerleave"].forEach((eventName) => {
        marquee.addEventListener(eventName, () => {
            pointerActive = false;
            pauseBriefly(500);
        });
    });

    marquee.addEventListener("focusin", () => pauseBriefly(1600));
    marquee.addEventListener("focusout", () => pauseBriefly(400));

    if ("IntersectionObserver" in window) {
        const observer = new IntersectionObserver(([entry]) => {
            sectionVisible = entry.isIntersecting;
            lastTime = performance.now();
        }, { threshold: 0.05 });
        observer.observe(marquee);
    }

    window.addEventListener("resize", normalizePosition);
    marquee.scrollLeft = groupWidth();
    animationFrame = window.requestAnimationFrame(tick);
    window.addEventListener("pagehide", () => window.cancelAnimationFrame(animationFrame), { once: true });
});
