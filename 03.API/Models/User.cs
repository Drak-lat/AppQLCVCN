namespace API.CongViec.Models;

public class User
{
    public int Id { get; set; }
    public string Username { get; set; }
    public string Password { get; set; }
    public string? FullName { get; set; }
    public string? Email { get; set; }
    public string? Phone { get; set; }
    public DateTime? Birthdate { get; set; }
    public string? Address { get; set; }
    public string? AvatarUrl { get; set; }

    // Fix: Add the missing 'Plans' property to resolve CS1061  
    public ICollection<Plan>? Plans { get; set; }
}
