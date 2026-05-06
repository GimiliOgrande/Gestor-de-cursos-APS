document.getElementById("registerForm").addEventListener("submit", function (event) {
    event.preventDefault();

    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;
    const categoria = document.getElementById("categoria").value;

    fetch("http://localhost:7000/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
            nome: nome,
            email: email,
            senha: senha,
            categoria: categoria
        })
    })
    .then(response => response.text())
    .then(data => {
        alert(data);
        window.location.href = "/login";
    })
    .catch(error => console.error(error));
});