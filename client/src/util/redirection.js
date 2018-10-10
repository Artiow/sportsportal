export default function redirect(history, newPath) {
    history.push(newPath)
}

export function safeRedirect(history, redirectId, newPath) {
    localStorage.setItem(`rd_${redirectId}`, window.location.pathname);
    redirect(history, newPath);
}

export function safeReverseRedirect(history, redirectId) {
    const key = `rd_${redirectId}`;
    const newPath = localStorage.getItem(key);
    if (newPath != null) {
        localStorage.removeItem(key);
        redirect(history, newPath);
    }
}