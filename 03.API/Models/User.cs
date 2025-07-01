namespace API.CongViec.Models;

public class User
{
    public int Id { get; set; }
    public string Username { get; set; } = string.Empty;
    public string Password { get; set; } = string.Empty;

    // Thông tin mở rộng cho Profile
    public string? FullName { get; set; } = string.Empty;
    public string? Email { get; set; } = string.Empty;
    public string? Phone { get; set; } = string.Empty;
    public DateTime? Birthdate { get; set; }       // Có thể để null nếu không nhập
    public string? Address { get; set; } = string.Empty;
    public string? AvatarUrl { get; set; } = string.Empty;
}
